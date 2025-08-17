package com.liajay.flightbooking.service;

import com.liajay.flightbooking.dal.dataobject.User;
import com.liajay.flightbooking.service.dto.UserLoginDTO;
import com.liajay.flightbooking.service.dto.UserRegisterDTO;
import com.liajay.flightbooking.service.dto.result.UserLoginResultDTO;
import com.liajay.flightbooking.service.dto.result.UserRegisterResultDTO;

/**
 * 用户服务接口
 * Service层 - 业务逻辑接口
 */
public interface UserService {
    
    /**
     * 用户注册
     */
    UserRegisterResultDTO register(UserRegisterDTO registerDTO);
    
    /**
     * 用户登录
     */
    UserLoginResultDTO login(UserLoginDTO loginDTO);
    
    /**
     * 根据用户名查询用户
     */
    User findByUsername(String username);

}
