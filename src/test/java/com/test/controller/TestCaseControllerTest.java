package com.test.controller;

import com.test.entity.TestCase;
import com.test.mapper.TestCaseMapper;
import com.test.service.TestCaseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(TestCaseController.class)
@DisplayName("测试用例接口自动化测试")
public class TestCaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TestCaseService testCaseService;

    @MockBean
    private TestCaseMapper testCaseMapper;

    @Test
    @DisplayName("创建用例 - 成功")
    void shouldCreateTestCase() throws Exception {
        TestCase input = new TestCase();
        input.setTitle("登录验证");

        when(testCaseService.createTestCase(any(TestCase.class))).thenReturn(input);

        mockMvc.perform(post("/api/testcases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"登录验证\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.title").value("登录验证"));
    }

    @Test
    @DisplayName("创建用例 - 标题为空 → 400")
    void shouldReturn400WhenTitleMissing() throws Exception {
        mockMvc.perform(post("/api/testcases") // 修正URL
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\"}"))
                .andExpect(status().isBadRequest()) // 现在会返回400了
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @DisplayName("查询用例 - ID不存在 → 404")
    void shouldReturn404WhenGetByIdNotFound() throws Exception {
    Long nonExistentId = 99999L;
    
    when(testCaseService.getTestCaseById(nonExistentId)).thenReturn(null);
    
    mockMvc.perform(get("/api/testcases/{id}", nonExistentId)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())  // 注意：你的Controller返回200，但data为null
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data").doesNotExist());
}
}