package com.huawei.cse.porter.file.endpoint;

import org.apache.servicecomb.foundation.common.utils.JsonUtils;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.swagger.invocation.context.ContextUtils;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.huawei.cse.porter.file.service.FileService;
import com.huawei.cse.porter.user.dao.SessionInfo;

@RestSchema(schemaId = "file")
@RequestMapping(path = "/")
public class FileServiceEndpoint {
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
        String session = ContextUtils.getInvocationContext().getContext("session-info");
        if (session == null) {
            throw new InvocationException(403, "", "not allowed");
        } else {
            SessionInfo sessionInfo = null;
            try {
                sessionInfo = JsonUtils.readValue(session.getBytes("UTF-8"), SessionInfo.class);
            } catch (Exception e) {
                throw new InvocationException(403, "", "session not allowed");
            }
            if (sessionInfo == null || !sessionInfo.getRoleName().equals("admin")) {
                throw new InvocationException(403, "", "not allowed");
            }
        }
        return fileService.deleteFile(id);
    }
}
