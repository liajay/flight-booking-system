package com.liajay.flightbooking.user.service;

import com.liajay.flightbooking.user.dal.dataobject.User;
import com.liajay.flightbooking.user.service.dto.UserLoginDTO;
import com.liajay.flightbooking.user.service.dto.UserRegisterDTO;
import com.liajay.flightbooking.user.service.dto.result.UserLoginResultDTO;
import com.liajay.flightbooking.user.service.dto.result.UserRegisterResultDTO;

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
