package cn.newstrength.wcms.sysadmin.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import cn.newstrength.wtdf.plugin.result.PageableResult;
import cn.newstrength.wtdf.web.mybatis.service.impl.MapperServiceImpl;
import com.github.pagehelper.PageRowBounds;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.newstrength.nsms.dto.XObject;
import cn.newstrength.wcms.core.constant.Action;
import cn.newstrength.wcms.core.constant.Constants;
import cn.newstrength.wcms.core.constant.FlowApprovalResult;
import cn.newstrength.wcms.core.constant.WorkFlowType;
import cn.newstrength.wcms.core.plugin.ReadParam;
import cn.newstrength.wcms.sysadmin.flow.model.FsWorkflowQueueDefinition;
import cn.newstrength.wcms.sysadmin.fs.FsStatus;
import cn.newstrength.wcms.sysadmin.fs.FsType;
import cn.newstrength.wcms.sysadmin.fs.model.FilePart;
import cn.newstrength.wcms.sysadmin.fs.obj.FsObj;
import cn.newstrength.wcms.sysadmin.fs.util.FsUtils;
import cn.newstrength.wcms.sysadmin.queue.StartFsWorkflowQueue;
import cn.newstrength.wcms.sysadmin.site.obj.SiteObj;
import cn.newstrength.wtdf.plugin.obj.JWTSubject;
import cn.newstrength.wtdf.plugin.param.TranParams;
import cn.newstrength.wtdf.plugin.result.FailureTranResult;
import cn.newstrength.wtdf.plugin.result.SuccessTranResult;
import cn.newstrength.wtdf.plugin.result.TranResult;
import cn.newstrength.wtdf.plugin.service.DictionaryService;
import cn.newstrength.wtdf.plugin.service.RedisService;
import cn.newstrength.wtdf.plugin.tool.FileTools;
import cn.newstrength.wtdf.plugin.widm.DictionaryObj;

/**
 * 文件管理模块服务
 * 
 * @author xd
 *
 */
@Service(value = "fileSystemService")
public class FileSystemService {
	private static final Logger logger = LoggerFactory.getLogger(FileSystemService.class);
	private static final String BASE_STATICS_PATH = Constants.Repository.STATICS;
	// 已发布目录
	private static final String PUBLISHED = "/published";
	// 未发布目录
	private static final String UNPUBLISHED = "/unpublished";
	private static final String SEPARATOR = "/";
	private static final String ROOT = "root";
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	@Autowired
	private SiteService siteService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private StartFsWorkflowQueue startFsWorkflowQueue;

	private boolean isFlow() {
		DictionaryService dictionaryService = new DictionaryService(redisService);
		DictionaryObj dictionaryObj = dictionaryService.getSys("RESOURCE_APPROVAL");
		if (dictionaryObj == null) {
			logger.warn("💩 未获取到流程启用开关字典对象，请检查 系统参数 -> RESOURCE_APPROVAL");
			return false;
		}
		String value = dictionaryObj.getDicValue();
		if ("true".equalsIgnoreCase(value)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取静态文件默认存储的物理路径,从系统配置中获取
	 * 
	 * @param redisService
	 * @return
	 */
	public String getRepository() {
		return FsUtils.getPrettifyPath(ReadParam.getInstance().getParamValue(redisService, BASE_STATICS_PATH));
	}

	/**
	 * 获取未发布完整路径
	 * 
	 * @param siteCode 站点编码
	 * @param orgCode  机构编码
	 * @param path     当前相对路径
	 * @return
	 */
	public String getUnpublishedPath(String siteCode, String orgCode, String path) {
		String filePath = (getRepository() + UNPUBLISHED + SEPARATOR + siteCode + SEPARATOR + orgCode + SEPARATOR
				+ path);
		return FsUtils.getPrettifyPath(filePath);
	}

	/**
	 * 获取发布完整路径
	 * 
	 * @param siteCode 站点编码
	 * @param orgCode  机构编码
	 * @param path     当前相对路径
	 * @return
	 */
	public String getPublishedPath(String siteCode, String orgCode, String path) {
		String filePath = (getRepository() + PUBLISHED + SEPARATOR + siteCode + SEPARATOR + orgCode + SEPARATOR + path);
		return FsUtils.getPrettifyPath(filePath);
	}

	/**
	 * 数据库存储路径，相对于 /siteCode/orgCode 前缀
	 * 
	 * @param path
	 * @return
	 */
	public String getFilePath(String path) {
		return FsUtils.getPrettifyPath(path);
	}

	/**
	 * 获取文件列表
	 * 
	 * @param jwt
	 * @param siteId
	 * @param path
	 * @param hidden
	 * @return
	 */
	public TranResult<List<FilePart>> ls(JWTSubject jwt, Long siteId, String path, boolean hidden,int currentPage,int pageSize) {
		List<FilePart> data = new ArrayList<>();
		HashMap map = new HashMap();
		String orgCode = MapUtils.getString(jwt.getOther(), "orgCode", "");
		String filePath = FsUtils.getPrettifyPath(path);
		// 获取站点对象
		TranResult<SiteObj> selectResult = siteService.selectById(siteId);
		if (selectResult.getErrCode() != 0) {
			return new FailureTranResult<>(selectResult.getErrMsg());
		}
		XObject query = new XObject().add("siteId", siteId).add("parentPath", filePath);
		if (!ROOT.equals(jwt.getLoginId())) {
			query.add("orgCode", orgCode);
		}
		int offset = (currentPage - 1) * pageSize;
		PageRowBounds pageRowBounds = new PageRowBounds(offset, pageSize);
		List<FsObj> files = sqlSessionTemplate.selectList("wcms.filesystem.query", query,pageRowBounds);
		int pageCount = (int)((pageRowBounds.getTotal() - 1L) / (long)pageSize + 1L);
		PageableResult<List<Map<String, Object>>> pr = new PageableResult();
		pr.setCurrentPage(currentPage);
		pr.setPageSize(pageSize);
		pr.setRowCount(pageRowBounds.getTotal());
		pr.setPageCount(pageCount);
		for (FsObj item : files) {
			FilePart filePart = new FilePart();
			filePart.setId(item.getId());
			filePart.setOwner(item.getOwner());
			filePart.setFilename(item.getFileName());
			filePart.setExtension(item.getFileExtension());
			filePart.setLastModifiedTime(item.getCreateTime());
			filePart.setPath(item.getFilePath());
			filePart.setDirectory("FOLDER".equals(item.getFileType()) ? true : false);
			filePart.setStatus(item.getStatus());
			filePart.setRemark(item.getRemark());
			if (!filePart.isDirectory()) {
				filePart.setSize(FileTools.convertSize(item.getFileSize()));
			}
			data.add(filePart);
		}
        map.put("data",data);
		map.put("rows",pr);
		return new SuccessTranResult(map);
	}

	/**
	 * 创建目录
	 * 
	 * @param jwt
	 * @param siteId
	 * @param parentId
	 * @param folderName
	 * @return
	 */
	public TranResult<FilePart> createFolder(JWTSubject jwt, Long siteId, String path) {
		String filePath = FsUtils.getPrettifyPath(path);
		if (SEPARATOR.equals(filePath)) {
			return new FailureTranResult<>("请填写目录名称");
		}
		// 获取站点对象
		TranResult<SiteObj> selectResult = siteService.selectById(siteId);
		if (selectResult.getErrCode() != 0) {
			return new FailureTranResult<>(selectResult.getErrMsg());
		}
		SiteObj siteObj = selectResult.getData();
		String orgCode = MapUtils.getString(jwt.getOther(), "orgCode", "");
		// 查询创建的目录是否存在
		XObject query = new XObject().add("siteId", siteId).add("orgCode", orgCode).add("filePath", filePath);
		FsObj fsObj = sqlSessionTemplate.selectOne("wcms.filesystem.query", query);
		if (fsObj != null) {
			return new FailureTranResult<>("目录已经存在");
		}
		// 获取上级目录
		File parent = new File(filePath).getParentFile();
		String parentPath = FsUtils.getPrettifyPath(parent.getPath());
		if (!SEPARATOR.equals(parentPath)) {
			query.add("filePath", parentPath);
			fsObj = sqlSessionTemplate.selectOne("wcms.filesystem.query", query);
			if (fsObj == null) {
				logger.warn("查询上级目录参数信息：{}", query.toJSONString());
				return new FailureTranResult<>("上级目录不存在");
			}
		}
		// 构造目录物理路径
		File unpublished = new File(getUnpublishedPath(siteObj.getCode(), orgCode, filePath));
		unpublished.mkdirs();
		// 写入数据库
		fsObj = FsUtils.createFsObj(unpublished);
		fsObj.setSameId(fsObj.getId());
		fsObj.setFilePath(filePath).setParentPath(parentPath);
		fsObj.setSiteId(siteId).setSiteCode(siteObj.getCode()).setOrgCode(orgCode).setOwner(jwt.getLoginId());
		fsObj.setStatus(FsStatus.PUBLISHED.getStatus());
		sqlSessionTemplate.insert("wcms.filesystem.insert", fsObj);
		// 返回数据
		FilePart part = new FilePart();
		part.setId(fsObj.getId());
		part.setId(fsObj.getId());
		part.setFilename(unpublished.getName());
		part.setDirectory(true);
		part.setPath(filePath);
		part.setOwner(fsObj.getOwner());
		part.setLastModifiedTime(fsObj.getCreateTime());
		return new SuccessTranResult<>(part);
	}

	/**
	 * 删除文件，规则如下
	 * <ol>
	 * <li>根据文件 filePath 查询用户所属部门的文件数据</li>
	 * <li>如果不是本人创建，不允许删除</li>
	 * <li>流程中的数据，不允许删除</li>
	 * <li>删除同一批文件（zip解压缩后属于一批文件）</li>
	 * <li>删除 unpublished、published目录下对应的文件，删除数据库记录</li>
	 * </ol>
	 * 
	 * @param jwt
	 * @param siteId
	 * @param filePath
	 * @return
	 */
	public TranResult<Boolean> deleteQuietly(JWTSubject jwt, Long siteId, String id) {
		XObject query = new XObject().add("siteId", siteId).add("id", id);// .add("orgCode", orgCode);
		// 查询当前对象
		FsObj fsObj = sqlSessionTemplate.selectOne("wcms.filesystem.query", query);
		if (fsObj == null) {
			return new FailureTranResult<>("文件信息不存在");
		}
		// 不允许删除其他用户创建的文件
		if (!fsObj.getOwner().equals(jwt.getLoginId())) {
			return new FailureTranResult<>(String.format("当前 %s 路径非当前用户创建或包含他人创建的文件", fsObj.getFilePath()));
		}
		// 流程中的文件不能删除
		if (FsType.FILE.name().equals(fsObj.getFileType())) {
			if (FsStatus.PROCESS.getStatus().equals(fsObj.getStatus())) {
				return new FailureTranResult<>("流程中的文件无法删除");
			}
		}
		// 如果是目录，需要检查目录下是否存在文件，如果存在，则不允许删除
		if (FsType.FOLDER.name().equals(fsObj.getFileType())) {
			query.add("parentFilePath", fsObj.getFilePath());
			query.add("siteCode", fsObj.getSiteCode());
			int totalFiles = sqlSessionTemplate.selectOne("wcms.filesystem.count", query);
			if(totalFiles > 0) {
				return new FailureTranResult<>(String.format("路径 %s 下存在文件，请首先删除", fsObj.getFilePath()));
			}
		}
		deleteQuietly(fsObj);
		return new SuccessTranResult<>(true);
	}

	/**
	 * 物理删除文件信息
	 * 
	 * @param fsObj 文件对象
	 */
	private void deleteQuietly(FsObj fsObj) {
		File unpublished = new File(getUnpublishedPath(fsObj.getSiteCode(), fsObj.getOrgCode(), fsObj.getFilePath()));
		FileUtils.deleteQuietly(unpublished);
		File published = new File(getPublishedPath(fsObj.getSiteCode(), fsObj.getOrgCode(), fsObj.getFilePath()));
		FileUtils.deleteQuietly(published);
		sqlSessionTemplate.delete("wcms.filesystem.deleteById", fsObj);
	}

	/**
	 * 文件上传
	 * 
	 * @param request
	 * @param whitelist
	 * @param siteId
	 * @param path
	 * @return
	 */
	@Transactional
	public TranResult<Boolean> upload(JWTSubject jwt, HttpServletRequest request, String[] extensions, Long siteId,
									  String path, String sign, MapperServiceImpl service) {
		// 获取站点对象
		TranResult<SiteObj> selectResult = siteService.selectById(siteId);
		if (selectResult.getErrCode() != 0) {
			return new FailureTranResult<>(selectResult.getErrMsg());
		}
		SiteObj siteObj = selectResult.getData();
		String siteCode = siteObj.getCode();
		String orgCode = MapUtils.getString(jwt.getOther(), "orgCode", "");
		String parentPath = FsUtils.getPrettifyPath(path);
		// 获取上传文件对象
		List<MultipartFile> files = FsUtils.getMultipartFile(request);
		if (files.size() == 0) {
			return new FailureTranResult<>("请选择文件");
		}
		MultipartFile multipartFile = files.get(0);// 这里只支持单个文件上传

		HashMap inputs = new HashMap();
		inputs.put("parentPath",parentPath);
		inputs.put("fileName",multipartFile.getOriginalFilename());
		TranParams params = new TranParams(inputs);
		params.setResource("wcms.filesystem.selectByNamePath");
		TranResult<FsObj> tranResult = service.selectOne(params);
		FsObj res = tranResult.getData();
		if(res!=null) {
			if (res.getFileName().equals(multipartFile.getOriginalFilename())) {
				return new FailureTranResult<>("不能上传重名文件，请重新上传");
			}
		}
		// 校验上传文件合法性
		TranResult<Boolean> checkResult = checkUpload(multipartFile, extensions);
		if (checkResult.getErrCode() != 0) {
			return checkResult;
		}
		String filename = multipartFile.getOriginalFilename();
		// 上传文件
		File unpublished = new File(getUnpublishedPath(siteCode, orgCode, parentPath), filename);
		File tmp = unpublished.getParentFile();
		if (!tmp.exists()) {
			tmp.mkdirs();
		}
		boolean succ = FsUtils.transferTo(multipartFile, unpublished);
		if (!succ) {
			return new FailureTranResult<>("文件上传失败了,请检查");
		}
		// 构造一个传递数据对象
		FsObj data = new FsObj();
		data.setSiteId(siteId).setSiteCode(siteCode).setOrgCode(orgCode).setOwner(jwt.getLoginId())
				.setParentPath(parentPath);
		return uploadAfter(unpublished, data,sign);
	}

	/**
	 * 文件上传后业务逻辑
	 * 
	 * @param extension
	 * @return
	 */
	private TranResult<Boolean> uploadAfter(File unpublished, FsObj data,String sign) {
		List<FsObj> result = new ArrayList<>(); // 定义文件集合
		String extension = FilenameUtils.getExtension(unpublished.getName());
		File target = new File(unpublished.getPath());
		String filePath = FsUtils.getPrettifyPath(data.getParentPath() + "/" + unpublished.getName());
		if ("zip".equalsIgnoreCase(extension) && sign.equals("0")) {// 如果是zip文件，则自动解压
			TranResult<File> unzipResult = FsUtils.unzip(unpublished, true); // 这里参数 true表示上传成功后，删除源文件;解压异常直接删除上传的文件
			if (unzipResult.getErrCode() != 0) {
				return new FailureTranResult<>("文件解压缩失败");
			}
			// 解压后的目录 d:/demo/a.zip 转换为 d:/demo/a
			target = unzipResult.getData();
			filePath = FsUtils.getPrettifyPath(data.getParentPath() + "/" + target.getName());
		}else if("zip".equalsIgnoreCase(extension) && sign.equals("1")){
			filePath = FsUtils.getPrettifyPath(data.getParentPath() + "/" + target.getName());
		}
		// 创建FsObj对象
		FsObj fsObj = FsUtils.createFsObj(target);
		fsObj.setSameId(fsObj.getId());
		fsObj.setFilePath(filePath);
		fsObj.setParentPath(data.getParentPath());
		FsUtils.getFiles(result, fsObj, target); // 递归构造数据
		// 是否有流程
		boolean flow = isFlow();
		if (!flow) { // 写入数据并将 unpublished 文件 发布到 published
			result.forEach(item -> {
				item.setSiteCode(data.getSiteCode()).setOrgCode(data.getOrgCode()).setOwner(data.getOwner())
						.setSiteId(data.getSiteId()).setStatus(FsStatus.PUBLISHED.getStatus());
			});
			sqlSessionTemplate.insert("wcms.filesystem.batchInsert", result);
			// 将文件拷贝到发布目录
			File published = new File(getPublishedPath(data.getSiteCode(), data.getOrgCode(), data.getParentPath()),
					target.getName());
			boolean succ = FsUtils.copyDirectoryQuietly(unpublished, published);
			if (succ) {
				logger.info("文件 {} 转移到 {} 成功!", unpublished.getPath(), published.getPath());
			}
		} else { // 需要流程
			result.forEach(item -> {
				item.setSiteCode(data.getSiteCode()).setOrgCode(data.getOrgCode()).setOwner(data.getOwner())
						.setSiteId(data.getSiteId()).setStatus(FsStatus.PROCESS.getStatus());
			});
			sqlSessionTemplate.insert("wcms.filesystem.batchInsert", result);
			// 发起流程请求
			WorkFlowType workFlowType = WorkFlowType.FS_FILE_ADD;
			TranResult<Boolean> startResult = strtWorkFlow(data.getOwner(), fsObj.getId(), Action.ADD, workFlowType);
			if (startResult.getErrCode() != 0) {
				throw new RuntimeException("流程启动失败");
			}
		}
		return new SuccessTranResult<>(true);
	}

	/**
	 * 文件发布
	 * 
	 * @param id
	 * @return
	 */
	public TranResult<Boolean> doPublish(FsObj fsObj) {
		File unpublished = new File(getUnpublishedPath(fsObj.getSiteCode(), fsObj.getOrgCode(), fsObj.getFilePath()));
		File published = new File(getPublishedPath(fsObj.getSiteCode(), fsObj.getOrgCode(), fsObj.getFilePath()));
		boolean succ = FsUtils.copyQuietly(unpublished, published);
		if (!succ) {
			logger.info("源地址：{}", unpublished.getPath());
			logger.info("目的地址：{}", published.getPath());
			return new FailureTranResult<>("文件发布失败了");
		}
		// 更新状态为发布状态
		String sameId = fsObj.getSameId();
		XObject query = new XObject().add("sameId", sameId);
		List<FsObj> files = sqlSessionTemplate.selectList("wcms.filesystem.query", query);
		files.forEach(item -> {
			query.add("id", item.getId()).add("status", FsStatus.PUBLISHED.getStatus());
			sqlSessionTemplate.update("wcms.filesystem.updateStatus", query);
		});
		return new SuccessTranResult<>(true);
	}

	public TranResult<Boolean> doPublish(String id) {
		XObject query = new XObject().add("id", id);
		FsObj fsObj = sqlSessionTemplate.selectOne("wcms.filesystem.query", query);
		if (fsObj == null) {
			return new FailureTranResult<>("文件信息不存在");
		}
		return doPublish(fsObj);
	}

	/**
	 * 流程启动
	 * 
	 * @param loginId
	 * @param fsId
	 * @param workFlowType
	 * @return
	 */
	private TranResult<Boolean> strtWorkFlow(String loginId, String fsId, Action action, WorkFlowType workFlowType) {
		FsWorkflowQueueDefinition definition = new FsWorkflowQueueDefinition();
		definition.setId(fsId);
		definition.setAction(action);
		definition.setWorkFlowType(workFlowType);
		TranResult<Boolean> tranResult = startFsWorkflowQueue.send(loginId, definition);
		logger.info("流程已启动！结果: {}", tranResult.getData());
		return tranResult;
	}

	/**
	 * 文件上传校验
	 * 
	 * @param multipartFile 上传文件对象
	 * @param extensions    允许的扩展名
	 * @return
	 */
	private TranResult<Boolean> checkUpload(MultipartFile multipartFile, String[] extensions) {
		String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
		if (StringUtils.isBlank(extension)) {
			return new FailureTranResult<>("文件后缀不能为空");
		}
		if (!ArrayUtils.contains(extensions, extension.toLowerCase())) {
			return new FailureTranResult<>("暂时不支持文件类型");
		}
		return new SuccessTranResult<>(true);
	}

	/**
	 * 获取待审批文件清单
	 * 
	 * @param id
	 * @return
	 */
	public TranResult<Map<String, Object>> queryForFlow(String id) {
		List<XObject> files = new ArrayList<>();
		XObject query = new XObject().add("id", id);
		Map<String, Object> info = sqlSessionTemplate.selectOne("wcms.filesystem.queryInfo", query);
		if (info == null) {
			logger.info("查询文件id = {}", id);
			return new FailureTranResult<>("文件不存在");
		}
		// 获取预览地址
		DictionaryService dictionaryService = new DictionaryService(redisService);
		DictionaryObj dictionaryObj = dictionaryService.getSys("RESOURCE_APPROVAL");
		String previewUrl = "#";
		if (dictionaryObj != null) {
			String ext2 = dictionaryObj.getExt2();
			if (StringUtils.isNotBlank(ext2)) {
				previewUrl = ext2;
			}
		}
		query.add("path", info.get("filePath")).add("owner", info.get("owner")).remove("id");
		List<FsObj> fsObjs = sqlSessionTemplate.selectList("wcms.filesystem.query", query);
		for (FsObj fsObj : fsObjs) {
			XObject row = new XObject();
			row.add("filePath", fsObj.getFilePath());
			if ("FOLDER".equals(fsObj.getFileType())) {
				row.add("fileUrl", "#");
			} else {
				StringBuffer sb = new StringBuffer(previewUrl);
				sb.append("/")
				.append(fsObj.getSiteCode())
				.append("/")
				.append(fsObj.getOrgCode())
				.append(fsObj.getFilePath());
				row.add("fileUrl", sb.toString());
			}
			files.add(row);
		}
		info.put("files", files);
		return new SuccessTranResult<>(info);
	}

	/**
	 * 流程结束后的通知操作
	 * 
	 * @param params
	 * @return
	 */
	@Transactional
	public TranResult<Boolean> flowNotify(TranParams<Map<String, Object>> params) {
		Map<String, Object> input = params.getParam();
		logger.info("流程通知参数：{}", input.toString());
		String id = MapUtils.getString(input, "bizKey");
		String loginId = MapUtils.getString(input, "loginId");
		String action = MapUtils.getString(input, "action");
		String comment = MapUtils.getString(input, "flowApprovalComment");
		String flowApprovalResult = MapUtils.getString(input, "flowApprovalResult"); // 同意：agree 不同意：disagree
		XObject query = new XObject().add("id", id);
		FsObj fsObj = sqlSessionTemplate.selectOne("wcms.filesystem.query", query);
		if (fsObj == null) { // 未查询到文件信息
			logger.error("🕷️ 未查询到 {} 文件信息");
			return new SuccessTranResult<>(true);
		}
		if (action.equals(Action.ADD.getValue())) { // 上传文件
			if (FlowApprovalResult.AGREE.getValue().equals(flowApprovalResult)) {
				fsObj.setStatus(FsStatus.PUBLISHED.getStatus()); // 设置状态为发布状态
				fsObj.setRemark(FsUtils.join(";", fsObj.getRemark(), comment));
				sqlSessionTemplate.update("wcms.filesystem.updateStatus", fsObj);
				File unpublished = new File(
						getUnpublishedPath(fsObj.getSiteCode(), fsObj.getOrgCode(), fsObj.getFilePath()));
				File published = new File(
						getPublishedPath(fsObj.getSiteCode(), fsObj.getOrgCode(), fsObj.getFilePath()));
				FsUtils.copyQuietly(unpublished, published);
				sqlSessionTemplate.update("wcms.filesystem.updateStatus", fsObj);
			} else {
				fsObj.setRemark(String.format("审批人 %s 审批不通过，原因 %s", loginId, comment));
				fsObj.setStatus(FsStatus.UNPUBLISHED.getStatus()); // 设置状态为未发布状态
				sqlSessionTemplate.update("wcms.filesystem.updateStatus", fsObj);
			}
		}
		return new SuccessTranResult<>(true);
	}
}
