package com.liajay.flightbooking.user.web.controller;

import com.liajay.flightbooking.user.service.UserService;
import com.liajay.flightbooking.user.service.dto.UserLoginDTO;
import com.liajay.flightbooking.user.service.dto.UserRegisterDTO;
import com.liajay.flightbooking.user.service.dto.result.UserLoginResultDTO;
import com.liajay.flightbooking.user.service.dto.result.UserRegisterResultDTO;
import com.liajay.flightbooking.user.web.convertor.UserConvertor;
import com.liajay.flightbooking.user.web.request.UserLoginRequest;
import com.liajay.flightbooking.user.web.request.UserRegisterRequest;
import com.liajay.flightbooking.user.web.response.HttpResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;

/**
 * 用户控制器
 * Web层 - 处理HTTP请求和响应
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @PostMapping("/register")
    @ResponseBody
    public HttpResponse<UserRegisterResultDTO> register(@Valid @RequestBody UserRegisterRequest request) {
        UserRegisterDTO registerDTO = UserConvertor.req2dto(request);

        UserRegisterResultDTO resultDTO = userService.register(registerDTO);

        return HttpResponse.success(resultDTO);
    }

    /**
     * 登录不校验参数,由服务层负责
     * @param request
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public HttpResponse<UserLoginResultDTO> login(@RequestBody UserLoginRequest request) {
        UserLoginDTO registerDTO = UserConvertor.req2dto(request);

        UserLoginResultDTO resultDTO = userService.login(registerDTO);

        return HttpResponse.success(resultDTO);
    }


    @GetMapping("/health")
    @ResponseBody
    public HttpResponse<String> health() {
        return HttpResponse.success("User service is running");
    }


}
