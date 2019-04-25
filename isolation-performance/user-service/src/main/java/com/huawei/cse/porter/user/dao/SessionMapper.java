package com.huawei.cse.porter.user.dao;

public interface SessionMapper {
    void createSession(SessionInfo sessionInfo);

    SessionInfo getSessioinInfo(String sessionId);
    
    void updateSessionInfo(String sessionId);
}
