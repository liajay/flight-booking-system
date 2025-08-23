package com.liajay.flightbooking.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liajay.flightbooking.user.UserServiceApplication;
import com.liajay.flightbooking.user.dal.dataobject.User;
import com.liajay.flightbooking.user.web.request.UserLoginRequest;
import com.liajay.flightbooking.user.web.request.UserRegisterRequest;
import com.liajay.flightbooking.user.web.response.HttpResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * UserController 集成测试类
 * 使用完整的Spring上下文，连接真实数据库进行测试
 */
@SpringBootTest(classes = UserServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/users";
    private static final String TEST_USERNAME = "testuser123";
    private static final String TEST_PASSWORD = "TestPass@123";
    private static final String INVALID_USERNAME = "a"; // 太短的用户名
    private static final String INVALID_PASSWORD = "123"; // 太简单的密码

    //两个测试数据
    private final User testUser1 = new User("test1", "123456@abc");
    private final User testUser2 = new User("test2", "123456@abc");

    @Test
    @Order(1)
    @DisplayName("健康检查接口测试")
    public void testHealthCheck() throws Exception {
        mockMvc.perform(get(BASE_URL + "/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("User service is running"))
                .andExpect(jsonPath("$.message").doesNotExist());
    }

    @Test
    @Order(2)
    @DisplayName("用户注册成功测试")
    public void testRegisterSuccess() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername(TEST_USERNAME);
        request.setPassword(TEST_PASSWORD);

        MvcResult result = mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.success").value(true))
                .andReturn();

        // 验证返回的JSON结构
        String jsonResponse = result.getResponse().getContentAsString();
        HttpResponse<?> response = objectMapper.readValue(jsonResponse, HttpResponse.class);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    @Order(3)
    @DisplayName("用户注册参数校验测试 - 用户名为空")
    public void testRegisterWithEmptyUsername() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername("");
        request.setPassword(TEST_PASSWORD);

        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    @DisplayName("用户注册参数校验测试 - 密码为空")
    public void testRegisterWithEmptyPassword() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername(TEST_USERNAME);
        request.setPassword("");

        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    @DisplayName("用户注册参数校验测试 - 无效的用户名")
    public void testRegisterWithInvalidUsername() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername(INVALID_USERNAME);
        request.setPassword(TEST_PASSWORD);

        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(6)
    @DisplayName("用户注册参数校验测试 - 无效的密码")
    public void testRegisterWithInvalidPassword() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername(TEST_USERNAME);
        request.setPassword(INVALID_PASSWORD);

        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    @DisplayName("用户注册参数校验测试 - 请求体为空")
    public void testRegisterWithNullBody() throws Exception {
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(8)
    @DisplayName("用户登录成功测试")
    public void testLoginSuccess() throws Exception {
        // 先注册一个用户
        UserRegisterRequest registerRequest = createValidRegisterRequest();
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // 然后用相同的用户名和密码登录
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername(TEST_USERNAME);
        loginRequest.setPassword(TEST_PASSWORD);

        MvcResult result = mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists())
                .andReturn();

        // 验证返回的JSON结构
        String jsonResponse = result.getResponse().getContentAsString();
        HttpResponse<?> response = objectMapper.readValue(jsonResponse, HttpResponse.class);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    @Order(9)
    @DisplayName("用户登录失败测试 - 用户不存在")
    public void testLoginWithNonExistentUser() throws Exception {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("nonexistentuser");
        loginRequest.setPassword(TEST_PASSWORD);

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(10)
    @DisplayName("用户登录失败测试 - 密码错误")
    public void testLoginWithWrongPassword() throws Exception {
        // 先注册一个用户
        UserRegisterRequest registerRequest = createValidRegisterRequest();
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // 用错误的密码登录
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername(TEST_USERNAME);
        loginRequest.setPassword("WrongPassword@123");

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(11)
    @DisplayName("用户登录参数校验测试 - 用户名为空")
    public void testLoginWithEmptyUsername() throws Exception {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("");
        loginRequest.setPassword(TEST_PASSWORD);

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(12)
    @DisplayName("用户登录参数校验测试 - 密码为空")
    public void testLoginWithEmptyPassword() throws Exception {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername(TEST_USERNAME);
        loginRequest.setPassword("");

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(13)
    @DisplayName("用户重复注册测试")
    public void testDuplicateUserRegistration() throws Exception {
        UserRegisterRequest request = createValidRegisterRequest();

        // 第一次注册应该成功
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 第二次注册相同用户名应该失败
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(14)
    @DisplayName("完整的用户注册和登录流程测试")
    public void testCompleteUserFlow() throws Exception {
        String uniqueUsername = "flowtest" + System.currentTimeMillis();
        
        // 1. 注册用户
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setUsername(uniqueUsername);
        registerRequest.setPassword(TEST_PASSWORD);

        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 2. 登录用户
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername(uniqueUsername);
        loginRequest.setPassword(TEST_PASSWORD);

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @Order(15)
    @DisplayName("HTTP方法不支持测试")
    public void testUnsupportedHttpMethods() throws Exception {
        // 测试不支持的HTTP方法
        mockMvc.perform(put(BASE_URL + "/register"))
                .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(delete(BASE_URL + "/register"))
                .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(patch(BASE_URL + "/login"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @Order(16)
    @DisplayName("Content-Type不支持测试")
    public void testUnsupportedContentType() throws Exception {
        UserRegisterRequest request = createValidRegisterRequest();

        // 使用不支持的Content-Type
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_XML)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @Order(17)
    @DisplayName("JSON格式错误测试")
    public void testMalformedJson() throws Exception {
        String malformedJson = "{\"username\":\"test\", \"password\":}"; // 缺少值

        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());
    }

    /**
     * 创建有效的注册请求对象
     */
    private UserRegisterRequest createValidRegisterRequest() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername(TEST_USERNAME + System.currentTimeMillis()); // 确保用户名唯一
        request.setPassword(TEST_PASSWORD);
        return request;
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }
}
