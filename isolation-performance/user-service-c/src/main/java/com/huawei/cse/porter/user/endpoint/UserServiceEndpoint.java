package com.huawei.cse.porter.user.endpoint;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.servicecomb.foundation.common.utils.JsonUtils;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.huawei.cse.porter.user.dao.SessionInfo;
import com.huawei.cse.porter.user.dao.SessionMapper;
import com.huawei.cse.porter.user.dao.UserInfo;
import com.huawei.cse.porter.user.dao.UserMapper;

@RestSchema(schemaId = "user")
@RequestMapping(path = "/")
public class UserServiceEndpoint {
    private static Logger LOGGER = LoggerFactory.getLogger(UserServiceEndpoint.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SessionMapper sessionMapper;

    /**
     * 登陆接口.
     */
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public SessionInfo login(@RequestParam(name = "userName") String userName,
            @RequestParam(name = "password") String password) {
        UserInfo userInfo = userMapper.getUserInfo(userName);
        if (userInfo != null) {
            if (validatePassword(password, userInfo.getPassword())) {
                SessionInfo sessionInfo = new SessionInfo();
                sessionInfo.setSessiondId(UUID.randomUUID().toString());
                sessionInfo.setUserName(userInfo.getUserName());
                sessionInfo.setRoleName(userInfo.getRoleName());
                sessionMapper.createSession(sessionInfo);
                return sessionInfo;
            }
        }
        return null;
    }

    @GetMapping(path = "/session", produces = MediaType.APPLICATION_JSON_VALUE)
    public SessionInfo getSession(@RequestParam(name = "sessionId") String sessionId) {
        if (sessionId == null) {
            throw new InvocationException(405, "", "invalid session.");
        }
        SessionInfo sessionInfo = sessionMapper.getSessioinInfo(sessionId);
        if (sessionInfo != null) {
            if (System.currentTimeMillis() - sessionInfo.getActiveTime().getTime() > 10 * 60 * 1000) {
                LOGGER.info("user session expired.");
                return null;
            } else {
                sessionMapper.updateSessionInfo(sessionInfo.getSessiondId());
            }
        }
        return sessionInfo;
    }

    private boolean validatePassword(String plain, String encrypt) {
        // 简单加密校验，开发者可以结合业务场景自行修改
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(plain.getBytes("UTF-8"));
            byte byteBuffer[] = messageDigest.digest();
            String encryptedText = Base64.encodeBase64String(byteBuffer);
            return encryptedText.equals(encrypt);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    // test code, remove it if necessary
    public static void main(String[] args) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update("test".getBytes("UTF-8"));
            byte byteBuffer[] = messageDigest.digest();
            String encryptedText = Base64.encodeBase64String(byteBuffer);
            System.out.println(encryptedText);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        try {
            SessionInfo sessionInfo = new SessionInfo();
            String xxx = JsonUtils.writeValueAsString(sessionInfo);
            SessionInfo t = JsonUtils.readValue(xxx.getBytes(), SessionInfo.class);
            System.out.println(t);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
