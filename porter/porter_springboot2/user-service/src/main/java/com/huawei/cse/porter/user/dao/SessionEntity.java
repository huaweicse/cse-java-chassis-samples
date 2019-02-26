package com.huawei.cse.porter.user.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.huawei.cse.porter.user.api.SessionInfo;

@Entity
@Table(name = "T_SESSION", schema = "porter_user_db")
public class SessionEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private int id;

  @Column(name = "SESSION_ID", unique = true, nullable = false, length = 64)
  private String sessiondId;

  @Column(name = "USER_NAME", unique = true, nullable = false, length = 64)
  private String userName;

  @Column(name = "ROLE_NAME", unique = false, nullable = false, length = 64)
  private String roleName;

  @Column(name = "CREATION_TIME", nullable = true)
  private java.sql.Timestamp creationTime = new java.sql.Timestamp(System.currentTimeMillis());

  @Column(name = "ACTIVE_TIME", nullable = true)
  private java.sql.Timestamp activeTime = new java.sql.Timestamp(System.currentTimeMillis());

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getSessiondId() {
    return sessiondId;
  }

  public void setSessiondId(String sessiondId) {
    this.sessiondId = sessiondId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public java.sql.Timestamp getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(java.sql.Timestamp creationTime) {
    this.creationTime = creationTime;
  }

  public java.sql.Timestamp getActiveTime() {
    return activeTime;
  }

  public void setActiveTime(java.sql.Timestamp activeTime) {
    this.activeTime = activeTime;
  }

  public static SessionInfo toSessionInfo(SessionEntity entity) {
    SessionInfo info = new SessionInfo();
    info.setSessiondId(entity.getSessiondId());
    info.setUserName(entity.getUserName());
    info.setRoleName(entity.getRoleName());
    return info;
  }
}
