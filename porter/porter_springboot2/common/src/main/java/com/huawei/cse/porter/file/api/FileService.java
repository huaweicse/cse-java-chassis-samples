package com.huawei.cse.porter.file.api;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
  public String uploadFile(MultipartFile file);

  public boolean deleteFile(String id);
}
