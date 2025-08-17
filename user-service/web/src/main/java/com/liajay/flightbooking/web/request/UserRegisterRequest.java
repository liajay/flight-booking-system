package com.liajay.flightbooking.web.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.liajay.flightbooking.util.validator.ValidPassword;
import com.liajay.flightbooking.util.validator.ValidUsername;

import javax.validation.constraints.NotBlank;

public class UserRegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @ValidUsername
    private String username;

    @NotBlank(message = "密码不能为空")
    @ValidPassword
    private String password;


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
