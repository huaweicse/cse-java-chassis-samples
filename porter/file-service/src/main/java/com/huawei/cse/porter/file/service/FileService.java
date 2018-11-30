package com.huawei.cse.porter.file.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 提供文件服务，可以有多个实现，比如文件、对象存储服务器、分布式文件存储等。
 */
public interface FileService {

    public String uploadFile(MultipartFile file);

    public boolean deleteFile(String id);

}
