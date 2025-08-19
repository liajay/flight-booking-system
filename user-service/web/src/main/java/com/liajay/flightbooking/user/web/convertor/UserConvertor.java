package com.liajay.flightbooking.user.web.convertor;

import com.liajay.flightbooking.user.service.dto.UserLoginDTO;
import com.liajay.flightbooking.user.service.dto.UserRegisterDTO;
import com.liajay.flightbooking.user.web.request.UserLoginRequest;
import com.liajay.flightbooking.user.web.request.UserRegisterRequest;

public class UserConvertor {
    public static UserLoginDTO req2dto(UserLoginRequest request) {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setUsername(request.getUsername());
        dto.setPassword(request.getPassword());
        return dto;
    }

    public static UserRegisterDTO req2dto(UserRegisterRequest registerDTO){
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername(registerDTO.getUsername());
        dto.setPassword(registerDTO.getPassword());
        return dto;
    }
}
