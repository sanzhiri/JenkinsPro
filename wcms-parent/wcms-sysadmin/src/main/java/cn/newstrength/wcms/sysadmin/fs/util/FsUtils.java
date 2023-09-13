package cn.newstrength.wcms.sysadmin.fs.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.UUID;
import cn.newstrength.wcms.sysadmin.fs.FsType;
import cn.newstrength.wcms.sysadmin.fs.obj.FsObj;
import cn.newstrength.wtdf.plugin.result.ExceptionTranResult;
import cn.newstrength.wtdf.plugin.result.FailureTranResult;
import cn.newstrength.wtdf.plugin.result.SuccessTranResult;
import cn.newstrength.wtdf.plugin.result.TranResult;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class FsUtils {
	private static final Logger logger = LoggerFactory.getLogger(FsUtils.class);

	public static String join(CharSequence delimiter, CharSequence... elements) {
		if (delimiter == null) {
			delimiter = ";";
		}
		if (elements == null) {
			return "";
		}
		StringJoiner joiner = new StringJoiner(delimiter);
		for (CharSequence cs : elements) {
			if (cs != null && cs.length() > 0) {
				joiner.add(cs);
			}
		}
		return joiner.toString();
	}

	public static void main(String[] args) {
		String url = "http://localhost:800/b";
		Console.log(getPrettifyUrl(url, "a", "b", "c"));
	}

	public static String getPrettifyUrl(CharSequence... elements) {
		if (elements == null) {
			throw new NullPointerException("URL 地址为空");
		}
		StringJoiner joiner = new StringJoiner("/");
		if (elements != null) {
			for (CharSequence cs : elements) {
				if (cs != null && cs.length() > 0) {
					joiner.add(cs);
				}
			}
		}
		return joiner.toString();
	}

	/**
	 * 获取一个相对路径的path，结尾不包含 /
	 * 
	 * @param path
	 * @return
	 */
	public static String getPrettifyPath(String path) {
		if (StringUtils.isBlank(path)) {
			return "/";
		}
		File file = new File("/", path);
		return file.getPath().replace('\\', '/');
	}

	public static boolean copyQuietly(File src, File dest) {
		if (!src.exists()) {
			logger.error("🪳 文件或目录 {} 不存在", src.getPath());
			return false;
		}
		try {
			if (src.isDirectory()) {
				FileUtils.copyDirectory(src, dest);
			} else {
				FileUtils.copyFile(src, dest);
			}
			return true;
		} catch (IOException e) {
			logger.error("🪳 文件拷贝异常：", e);
			return false;
		}
	}

	/**
	 * 文件拷贝
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public static boolean copyFileQuietly(File src, File dest) {
		try {
			if (!src.exists()) {
				logger.error("🪳 源文件 {} 不存在", src.getPath());
			}
			FileUtils.copyFile(src, dest);
			return true;
		} catch (IOException e) {
			logger.error("🪳 文件拷贝异常：", e);
		}
		return false;
	}

	/**
	 * 目录拷贝
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public static boolean copyDirectoryQuietly(File src, File dest) {
		try {
			if (!src.exists()) {
				logger.error("🪳 源文件 {} 不存在", src.getPath());
			}
			FileUtils.copyDirectory(src, dest);
			return true;
		} catch (IOException e) {
			logger.error("🪳 文件拷贝异常：", e);
		}
		return false;
	}

	/**
	 * 递归获取文件对象下的所有文件
	 * 
	 * @param result 返回的Fs文件对象集合
	 * @param sameId 标记同一批文件id
	 * @param path   source 文件对应的相对路径
	 * @param source 物理文件路径
	 */
	public static void getFiles(List<FsObj> result, FsObj fsObj, File source) {
		result.add(fsObj);
		File[] files = source.listFiles();
		if (files != null) {
			for (File file : files) {
				FsObj item = createFsObj(file);
				item.setSameId(fsObj.getSameId());
				item.setFilePath(getPrettifyPath((fsObj.getFilePath() + "/" + file.getName())));
				item.setParentPath(fsObj.getFilePath());
				if (file.isDirectory()) {
					getFiles(result, item, file);
				} else {
					result.add(item);
				}
			}
		}
	}

	/**
	 * 根据物理文件创建一个Fs文件对象，初始化对象基础值
	 * 
	 * @param source   物理文件对象
	 * @param parentId 输入书文件夹id
	 * @return
	 */
	public static FsObj createFsObj(File source) {
		if (!source.exists()) {
			return null;
		}
		FsObj fsObj = new FsObj();
		fsObj.setId(UUID.fastUUID().toString());
		fsObj.setFileName(source.getName()); // 文件或者目录名
		fsObj.setCreateTime(new Date(source.lastModified())); // 最后修改时间
		fsObj.setFileSize(source.length()); // 文件大小
		if (source.isDirectory()) {
			fsObj.setFileType(FsType.FOLDER.name());
		} else {
			fsObj.setFileType(FsType.FILE.name());
			fsObj.setFileExtension(FilenameUtils.getExtension(source.getName())); // 扩展名
		}
		return fsObj;
	}

	/**
	 * 文件加压缩，解压规则：解压后的路径为原路径+文件名。如 /home/wcms/sample/test.zip ->
	 * /home/wcms/sample/test/***
	 * 
	 * @param file         zip文件对象
	 * @param deleteSource 解压缩后是否删除原zip文件
	 * @return
	 */
	public static TranResult<File> unzip(File file, boolean deleteSource) {
		String baseName = FilenameUtils.getBaseName(file.getName());
		File target = new File(file.getParentFile().getPath(), baseName); // 自动创建文件夹
		ZipFile zipFile = new ZipFile(file);
		if (!zipFile.isValidZipFile()) {
			return new FailureTranResult<>("压缩文件可能被损坏.");
		}
		try {
			zipFile.extractAll(target.getPath());
			if (deleteSource) {
				FileUtils.deleteQuietly(file);
			}
		} catch (ZipException e) {
			FileUtils.deleteQuietly(file);
			return new ExceptionTranResult<>(e.getLocalizedMessage());
		}
		return new SuccessTranResult<>(target);
	}

	/**
	 * 获取文件上传对象，如果异常则返回null
	 * 
	 * @param request
	 * @return
	 */
	public static List<MultipartFile> getMultipartFile(HttpServletRequest request) {
		List<MultipartFile> files = new ArrayList<>();
		try {
			Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
			Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, MultipartFile> entry = it.next();
				files.add(entry.getValue());
			}
		} catch (Exception ex) {
			logger.error("获取MultipartFile对象异常：", ex);
		}
		return files;
	}

	/**
	 * 文件传输
	 * 
	 * @param multipartFile
	 * @param dest
	 * @return
	 */
	public static boolean transferTo(MultipartFile multipartFile, File dest) {
		try {
			multipartFile.transferTo(dest);
		} catch (IllegalStateException | IOException e) {
			logger.error("文件上传发生异常", e);
			return false;
		}
		return true;
	}
}
