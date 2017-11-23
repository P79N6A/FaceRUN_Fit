package com.fly.run.manager;

import com.fly.run.bean.AccountBean;

/**
 * Created by kongwei on 2017/3/10.
 */

public class UserInfoManager {

    private static UserInfoManager userInfoManager;
    private AccountBean accountBean = null;

    public static UserInfoManager getInstance() {
        if (userInfoManager == null)
            userInfoManager = new UserInfoManager();
        return userInfoManager;
    }

    public void setAccountInfo(AccountBean accountBean) {
        this.accountBean = accountBean;
    }

    public AccountBean getAccountInfo() {
        return accountBean;
    }

    public String getAccount() {
        return accountBean != null ? accountBean.getAccount() : "";
    }

    public void logout() {
        setAccountInfo(null);
    }

    public boolean isLogin() {
        if (getAccountInfo() != null)
            return true;
        return false;
    }

}
