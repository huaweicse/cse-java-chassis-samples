package com.huawei.cse.porter.user.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
  @Query(value = "select * from T_USER u where u.USER_NAME=?1", nativeQuery = true)
  UserEntity getUserInfo(String userName);
}
