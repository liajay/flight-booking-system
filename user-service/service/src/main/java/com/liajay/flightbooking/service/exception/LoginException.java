package com.liajay.flightbooking.service.exception;

import com.liajay.flightbooking.service.dto.UserLoginDTO;
import com.liajay.flightbooking.util.exception.BizException;


/**
 * 登录异常
 * @author liajay
 * @since 2025-8-15
 */
public class LoginException extends BizException {
    private final UserLoginDTO userLoginDTO;

    public LoginException(UserLoginDTO userLoginDTO) {
        this("", userLoginDTO);
    }

    public LoginException(String message, UserLoginDTO userLoginDTO) {
        super(String.format("用户%s登录失败, 原因: %s", userLoginDTO.getUsername(), message));
        this.userLoginDTO = userLoginDTO;
    }

    public UserLoginDTO getUserLoginDTO() {
        return userLoginDTO;
    }
}
