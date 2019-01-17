package com.huawei.cse.houseapp.user.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response.Status;

import org.apache.servicecomb.saga.omega.transaction.annotations.Compensable;
import org.apache.servicecomb.saga.omega.transaction.annotations.Participate;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.stereotype.Service;

import com.huawei.cse.houseapp.user.api.UserInfo;
import com.huawei.cse.houseapp.user.dao.UserMapper;

@Service
public class UserService {
  @Inject
  private UserMapper userMapper;

  @Compensable(compensationMethod = "cancelTransactionSaga")
  public boolean buyWithTransactionSaga(long userId, double price) {
    UserInfo info = userMapper.getUserInfo(userId);
    if (info == null) {
      throw new InvocationException(400, "", "user id not valid");
    }
    if (info.getTotalBalance() < price) {
      throw new InvocationException(400, "", "user do not got so mush money");
    }
    info.setTotalBalance(info.getTotalBalance() - price);
    userMapper.updateUserInfo(info);
    return true;
  }

  public boolean cancelTransactionSaga(long userId, double price) {
    // 进行补偿需要考虑事务状态：
    // 1. 如果已经扣款，那么需要加上款项；
    // 2. 如果没有扣款，那么不能增加款项；
    UserInfo info = userMapper.getUserInfo(userId);
    if (info == null) {
      throw new InvocationException(400, "", "user id not valid");
    }
    info.setTotalBalance(info.getTotalBalance() + price);
    userMapper.updateUserInfo(info);
    return true;
  }

  @Participate(confirmMethod = "confirmTransactionTCC", cancelMethod = "cancelTransactionTCC")
  public boolean buyWithTransactionTCC(long userId, double price) {
    UserInfo info = userMapper.getUserInfo(userId);
    if (info == null) {
      throw new InvocationException(Status.BAD_REQUEST, "user id not valid");
    }
    if (info.isReserved()) {
      throw new InvocationException(Status.BAD_REQUEST, "user have already reserved a house");
    }

    if (info.getTotalBalance() < price) {
      return false;
    } else {
      info.setReserved(true);
      userMapper.updateUserInfo(info);
      return true;
    }
  }

  void confirmTransactionTCC(long userId, double price) {
    UserInfo info = userMapper.getUserInfo(userId);
    if (info == null) {
      return;
    }
    if (info.isReserved()) {
      info.setReserved(false);
      info.setTotalBalance(info.getTotalBalance() - price);
      userMapper.updateUserInfo(info);
    }
  }

  void cancelTransactionTCC(long userId, double price) {
    UserInfo info = userMapper.getUserInfo(userId);
    if (info == null) {
      return;
    }
    if (info.isReserved()) {
      info.setReserved(false);
      userMapper.updateUserInfo(info);
    }
  }
}
