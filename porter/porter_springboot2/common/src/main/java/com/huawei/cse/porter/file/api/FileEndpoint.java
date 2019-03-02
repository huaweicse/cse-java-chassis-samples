package com.huawei.cse.porter.file.api;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@RestSchema(schemaId = "file")
@RequestMapping(path = "/")
public class FileEndpoint {
  @Autowired
  private FileService fileService;
  
  /**
   * 上传文件接口，用户上传一个文件，返回文件ID。
   */
  @PostMapping(path = "/upload", produces = MediaType.TEXT_PLAIN_VALUE)
  public String uploadFile(@RequestPart(name = "fileName") MultipartFile file) {
      return fileService.uploadFile(file);
  }

  /**
   * 删除文件接口。指定ID，返回删除成功还是失败.
   */
  @DeleteMapping(path = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
  public boolean deleteFile(@RequestParam(name = "id") String id) {
      return fileService.deleteFile(id);
  }
}
