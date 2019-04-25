package com.huawei.cse.porter.user.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huawei.cse.porter.user.api.SessionInfo;
import com.huawei.cse.porter.user.api.UserService;
import com.huawei.cse.porter.user.dao.SessionEntity;
import com.huawei.cse.porter.user.dao.SessionRepository;
import com.huawei.cse.porter.user.dao.UserEntity;
import com.huawei.cse.porter.user.dao.UserRepository;
import com.netflix.config.DynamicPropertyFactory;

@Service
public class UserServiceImpl implements UserService {
  private static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SessionRepository sessionRepository;

  public SessionInfo login(String userName,
      String password) {
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

  public SessionInfo getSession(String sessionId) {
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


  public String ping(String message) {
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
