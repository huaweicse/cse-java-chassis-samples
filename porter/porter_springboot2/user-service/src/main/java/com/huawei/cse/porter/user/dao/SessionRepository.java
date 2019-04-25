package com.huawei.cse.porter.user.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<SessionEntity, Integer> {
  @Query(value = "select * from T_SESSION s where s.SESSION_ID=?1", nativeQuery = true)
  SessionEntity getSessioinInfo(String sessionId);
}
