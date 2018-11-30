package com.huawei.cse.porter.user.endpoint;

import com.huawei.cse.porter.user.dao.UserInfo;

public class UserInfoView {
    private String userName;

    private String roleName;

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

    public static UserInfoView fromUserInfo(UserInfo userInfo) {
        UserInfoView view = new UserInfoView();
        view.setUserName(userInfo.getUserName());
        view.setRoleName(userInfo.getRoleName());
        return view;
    }
}
