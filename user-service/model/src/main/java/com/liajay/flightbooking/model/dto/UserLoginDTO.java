package com.liajay.flightbooking.model.dto;

import com.liajay.flightbooking.util.validator.ValidUsername;
import com.liajay.flightbooking.util.validator.ValidPassword;

/**
 * 用户登录数据传输对象
 * 
 * @author liajay
 */
public class UserLoginDTO {

    @ValidUsername(message = "用户名格式不正确")
    private String username;

    @ValidPassword(message = "密码格式不正确")
    private String password;

    public UserLoginDTO() {
    }

    public UserLoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
