package com.huawei.cse.porter.gateway;

import java.util.concurrent.CompletableFuture;

import com.huawei.cse.porter.user.api.SessionInfo;

public interface Session {
  public CompletableFuture<SessionInfo> getSession(String sessionId);
}
