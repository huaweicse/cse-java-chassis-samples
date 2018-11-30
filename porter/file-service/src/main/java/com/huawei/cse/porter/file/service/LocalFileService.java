package com.huawei.cse.porter.file.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 提供本地文件的上传和下载服务。这个作为测试使用。 因为本地文件不支持分布式同步，导致文件服务无法进行多实例部署。 
 * 由于测试使用，这里没有对文件大小、类型等做检查。使用者需要注意做好检查，包含系统安全。
 */
@Component
public class LocalFileService implements FileService {
    // maxmum BUFFER_SIZE * BUFFER_NUM
    private static final int BUFFER_SIZE = 10240;

    private static final File BASE_FILE = new File(".");

    @Override
    public String uploadFile(MultipartFile file) {
        byte[] buffer = new byte[BUFFER_SIZE];
        String fileId = UUID.randomUUID().toString();

        File outFile = new File(BASE_FILE, fileId);
        int len;
        try (InputStream is = file.getInputStream(); OutputStream os = new FileOutputStream(outFile)) {
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } catch (IOException e) {
            return null;
        }
        return fileId;
    }

    @Override
    public boolean deleteFile(String id) {
        File outFile = new File(BASE_FILE, id);
        return outFile.delete();
    }

}
