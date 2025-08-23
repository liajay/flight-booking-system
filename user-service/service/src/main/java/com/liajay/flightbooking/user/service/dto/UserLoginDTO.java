package com.liajay.flightbooking.user.service.dto;

import com.liajay.flightbooking.user.util.validator.ValidPassword;
import com.liajay.flightbooking.user.util.validator.ValidUsername;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录DTO
 */
public class UserLoginDTO {
    
    @NotBlank(message = "用户名不能为空")
    @ValidUsername
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @ValidPassword
    private String password;

    // Getter and Setter methods
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
