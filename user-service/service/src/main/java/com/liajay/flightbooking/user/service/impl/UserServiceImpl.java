package com.liajay.flightbooking.user.service.impl;

import com.liajay.flightbooking.user.service.dto.UserLoginDTO;
import com.liajay.flightbooking.user.service.dto.UserRegisterDTO;
import com.liajay.flightbooking.user.service.dto.result.UserLoginResultDTO;
import com.liajay.flightbooking.user.service.dto.result.UserRegisterResultDTO;
import com.liajay.flightbooking.user.service.exception.LoginException;
import com.liajay.flightbooking.user.util.JwtUtil;
import com.liajay.flightbooking.user.dal.dataobject.User;
import com.liajay.flightbooking.user.dal.repository.UserRepository;
import com.liajay.flightbooking.user.service.UserService;
import com.liajay.flightbooking.user.util.UserIdGenerator;
import com.liajay.flightbooking.user.util.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户服务实现类
 * Service层 - 业务逻辑实现
 */
@Service
public class UserServiceImpl implements UserService {
    
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserIdGenerator userIdGenerator;

    public UserServiceImpl(JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserIdGenerator userIdGenerator, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userIdGenerator = userIdGenerator;
        this.userRepository = userRepository;
    }

    //todo : 添加验证码
    @Override
    public UserRegisterResultDTO register(UserRegisterDTO registerDTO) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new BizException("用户已存在");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setUserId(userIdGenerator.generateNumericUserId());
        
        // 保存用户
        User savedUser = userRepository.save(user);
        UserRegisterResultDTO resultDTO = new UserRegisterResultDTO();
        resultDTO.setSuccess(true);
        log.info("用户注册成功: {}", savedUser.getUsername());

        return resultDTO;
    }
    //todo: 添加登录状态维护, 防止重放攻击
    @Override
    public UserLoginResultDTO login(UserLoginDTO loginDTO) {
        Optional<User> userOptional = userRepository.findByUsername(loginDTO.getUsername());
        if (!userOptional.isPresent()) {
            throw new LoginException("用户不存在", loginDTO);
        }
        
        User user = userOptional.get();

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new LoginException("密码错误", loginDTO);
        }

        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername());

        UserLoginResultDTO resultDTO = new UserLoginResultDTO();
        resultDTO.setToken(token);

        log.info("用户登录成功: {}", user.getUsername());
        
        return resultDTO;
    }
    
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

}
