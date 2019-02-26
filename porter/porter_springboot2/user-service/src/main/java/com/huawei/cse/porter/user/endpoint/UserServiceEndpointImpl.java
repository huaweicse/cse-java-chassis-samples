package com.huawei.cse.porter.user.endpoint;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
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

import com.huawei.cse.porter.user.api.SessionInfo;
import com.huawei.cse.porter.user.api.UserServiceEndpoint;
import com.huawei.cse.porter.user.dao.SessionEntity;
import com.huawei.cse.porter.user.dao.SessionRepository;
import com.huawei.cse.porter.user.dao.UserEntity;
import com.huawei.cse.porter.user.dao.UserRepository;
import com.netflix.config.DynamicPropertyFactory;

@RestSchema(schemaId = "user")
@RequestMapping(path = "/")
public class UserServiceEndpointImpl implements UserServiceEndpoint {
  private static Logger LOGGER = LoggerFactory.getLogger(UserServiceEndpointImpl.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SessionRepository sessionRepository;

  @PostMapping(path = "/v1/user/login", produces = MediaType.APPLICATION_JSON_VALUE)
  public SessionInfo login(@RequestParam(name = "userName") String userName,
      @RequestParam(name = "password") String password) {
    UserEntity userInfo = userRepository.getUserInfo(userName);
    if (userInfo != null) {
      if (validatePassword(password, userInfo.getPassword())) {
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setSessiondId(UUID.randomUUID().toString());
        sessionEntity.setUserName(userInfo.getUserName());
        sessionEntity.setRoleName(userInfo.getRoleName());
        sessionRepository.save(sessionEntity);
        return SessionEntity.toSessionInfo(sessionEntity);
      }
    }
    return null;
  }

  @GetMapping(path = "/v1/user/session", produces = MediaType.APPLICATION_JSON_VALUE)
  public SessionInfo getSession(@RequestParam(name = "sessionId") String sessionId) {
    if (sessionId == null) {
      throw new InvocationException(405, "", "invalid session.");
    }
    SessionEntity sessionEntity = sessionRepository.getSessioinInfo(sessionId);
    if (sessionEntity != null) {
      if (System.currentTimeMillis() - sessionEntity.getActiveTime().getTime() > 10 * 60 * 1000) {
        LOGGER.info("user session expired.");
        return null;
      } else {
        sessionRepository.save(sessionEntity);
        return SessionEntity.toSessionInfo(sessionEntity);
      }
    }
    return null;
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
      LOGGER.error("", e);
    } catch (UnsupportedEncodingException e) {
      LOGGER.error("", e);
    }
    return false;
  }


  @GetMapping(path = "/v1/user/ping", produces = MediaType.APPLICATION_JSON_VALUE)
  public String ping(@RequestParam(name = "message") String message) {
    long delay = DynamicPropertyFactory.getInstance().getLongProperty("user.ping.delay", 0).get();
    if (delay > 0) {
      try {
        TimeUnit.SECONDS.sleep(delay);
      } catch (InterruptedException e) {
        LOGGER.error("", e);
      }
    }
    return message;
  }
}
