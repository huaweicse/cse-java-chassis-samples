package com.huawei.cse.porter.user.dao;

import com.huawei.cse.porter.user.api.SessionInfo;

public interface SessionMapper {
    void createSession(SessionInfo sessionInfo);

    SessionInfo getSessioinInfo(String sessionId);
    
    void updateSessionInfo(String sessionId);
}
