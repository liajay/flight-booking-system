package com.liajay.flightbooking.user.model.vo;

/**
 * 登录响应视图对象
 * 
 * @author liajay
 */
public class LoginVO {

    private String token;
    private UserVO userInfo;

    public LoginVO() {
    }

    public LoginVO(String token, UserVO userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserVO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserVO userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "LoginVO{" +
                "token='[PROTECTED]'" +
                ", userInfo=" + userInfo +
                '}';
    }
}
