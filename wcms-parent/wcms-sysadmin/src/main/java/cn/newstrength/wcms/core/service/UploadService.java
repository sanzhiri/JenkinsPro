//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.newstrength.wcms.core.service;

import cn.newstrength.wcms.core.plugin.ReadParam;
import cn.newstrength.wcms.core.upload.UploadResult;
import cn.newstrength.wtdf.plugin.result.ExceptionTranResult;
import cn.newstrength.wtdf.plugin.result.FailureTranResult;
import cn.newstrength.wtdf.plugin.result.SuccessTranResult;
import cn.newstrength.wtdf.plugin.result.TranResult;
import cn.newstrength.wtdf.plugin.service.RedisService;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service("uploadService")
public class UploadService {
    private static final Logger logger = LoggerFactory.getLogger(UploadService.class);
    private static final String FILE_UPLOAD_PATH = "wcms.repository.resource";
    @Autowired
    private RedisService redisService;

    public UploadService() {
    }

    public TranResult<List<UploadResult>> upload(HttpServletRequest request, String extensions) {
        return this.upload(request, extensions, "");
    }

    public TranResult<List<UploadResult>> upload(HttpServletRequest request, String extensions, String platform) {
        if (extensions != null && !extensions.isEmpty()) {
            return "*".equals(extensions) ? this.upload(request, new String[0], platform) : this.upload(request, extensions.split(","), platform);
        } else {
            return new FailureTranResult("文件格式规则未配置.");
        }
    }

    public TranResult<List<UploadResult>> upload(HttpServletRequest request, String[] extensionArray, String platform) {
        List<UploadResult> files = new LinkedList();
        Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest)request).getFileMap();
        Iterator<Entry<String, MultipartFile>> it = fileMap.entrySet().iterator();
        String repository = ReadParam.getInstance().getParamValue(this.redisService, "wcms.repository.resource");
        if (repository != null && !repository.isEmpty()) {
            String folder = "/" + (new SimpleDateFormat("yyyy/MM")).format(new Date());

            while(it.hasNext()) {
                Entry<String, MultipartFile> entry = (Entry)it.next();
                MultipartFile multipartFile = (MultipartFile)entry.getValue();
                String originalFilename = multipartFile.getOriginalFilename();
                String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                if (extension == null || extension.isEmpty()) {
                    return new FailureTranResult("文件扩展名不能为空.");
                }

                if (extensionArray.length > 0) {
                    boolean allow = ArrayUtils.contains(extensionArray, extension.toLowerCase());
                    if (!allow) {
                        return new FailureTranResult("您上传的文件类型不正确.");
                    }
                }

                String contentType = multipartFile.getContentType();
                long size = multipartFile.getSize();
                String filename = String.valueOf(System.nanoTime()).concat(".").concat(extension);
                File parent = new File(repository, folder);
                if (!parent.exists()) {
                    parent.mkdirs();
                }

                File dest = new File(parent.getPath(), filename);
                boolean succ = transferTo(multipartFile, dest);
                if (!succ) {
                    return new FailureTranResult("文件上传失败.");
                }
                /*
                 * 20230128 by liujl
                 * add
                 * 为上传的文件添加其他用户的可读权限
                 * start
                 *  */
                try {
                    if (!System.getProperty("os.name").startsWith("Win")) {

                        String cmdGrant = "chmod 644 " + dest.getPath();

                        Runtime.getRuntime().exec(cmdGrant);//.exec(cmdGrant);

                    }
                }catch (IOException e){
                    return new FailureTranResult("上传文件赋权异常.");
                }
                /*
                 * 20230128 by liujl
                 * add
                 * 为上传的文件添加其他用户的可读权限
                 * end
                 *  */

                UploadResult uploadResult = new UploadResult();
                uploadResult.setContentType(contentType);
                uploadResult.setExtension(extension);
                uploadResult.setOriginalFilename(originalFilename);
                uploadResult.setSize(size);
                uploadResult.setFilename(filename);
                uploadResult.setFolder(folder);
                uploadResult.setPath(folder.concat("/").concat(filename));
                String resourceUrl = ReadParam.getInstance().getParamValue(this.redisService, "wcms.resource.url");
                if ("sysadmin".equalsIgnoreCase(platform)) {
                    resourceUrl = ReadParam.getInstance().getParamValue(this.redisService, "wcms.sysadmin.resource.url");
                }

                uploadResult.setUrl(resourceUrl.concat(folder.concat("/").concat(filename)));
                files.add(uploadResult);
            }

            return new SuccessTranResult(files);
        } else {
            return new FailureTranResult("文件上传目录未定义.");
        }
    }

    public TranResult<Boolean> simpleUpload(HttpServletRequest request, String[] blankArray, boolean unzip, File destDirectory) {
        Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest)request).getFileMap();
        Iterator it = fileMap.entrySet().iterator();

        while(it.hasNext()) {
            Entry<String, MultipartFile> entry = (Entry)it.next();
            MultipartFile multipartFile = (MultipartFile)entry.getValue();
            String filename = multipartFile.getOriginalFilename();
            String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            if (extension == null || extension.isEmpty()) {
                return new FailureTranResult("The file extension cannot be empty");
            }

            if (blankArray != null && blankArray.length > 0) {
                boolean allow = ArrayUtils.contains(blankArray, extension.toLowerCase());
                if (!allow) {
                    return new FailureTranResult("The file type you uploaded is not correct.");
                }
            }

            if (!destDirectory.exists()) {
                destDirectory.mkdirs();
            }

            File dest = new File(destDirectory.getPath(), filename);
            boolean succ = transferTo(multipartFile, dest);
            if (!succ) {
                return new FailureTranResult("File upload failed.");
            }

            if ("zip".equals(extension.toLowerCase()) && unzip) {
                TranResult<Boolean> tranResult = unzip(dest, true);
                if (tranResult.getErrCode() != 0) {
                    return new FailureTranResult(tranResult.getErrMsg());
                }
            }
        }

        return new SuccessTranResult(true);
    }

    public static TranResult<Boolean> unzip(File source, boolean deleteSource) {
        File target = new File(source.getParentFile().getPath());
        ZipFile zipFile = new ZipFile(source);
        if (!zipFile.isValidZipFile()) {
            return new FailureTranResult("压缩文件不合法,可能被损坏.");
        } else {
            logger.info("Charset is {} ", zipFile.getCharset().displayName());

            try {
                zipFile.extractAll(target.getPath());
                if (deleteSource) {
                    FileUtils.deleteQuietly(source);
                }
            } catch (ZipException var5) {
                var5.printStackTrace();
                logger.error("文件解压缩失败", var5);
                return new ExceptionTranResult(var5.getLocalizedMessage());
            }

            return new SuccessTranResult(true);
        }
    }

    protected static boolean transferTo(MultipartFile file, File dest) {
        try {
            file.transferTo(dest);
            return true;
        } catch (IOException | IllegalStateException var3) {
            logger.error("文件上传发生异常", var3);
            return false;
        }
    }
}

