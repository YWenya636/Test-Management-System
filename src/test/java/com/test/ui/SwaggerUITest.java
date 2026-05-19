package com.test.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.test.ui.page.SwaggerPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Swagger UI end-to-end test")
public class SwaggerUITest {
    private static final double SLOW_MOTION_MS = 1200;
    private static final double FINAL_REVIEW_MS = 8000;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private SwaggerPage swaggerPage;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();

        LaunchOptions options = new LaunchOptions();
        options.setHeadless(false);
        options.setSlowMo(SLOW_MOTION_MS);

        browser = playwright.chromium().launch(options);
        context = browser.newContext();
        page = context.newPage();
        swaggerPage = new SwaggerPage(page);
    }

    @Test
    @DisplayName("Run CRUD walkthrough from Swagger UI")
    void testCrudWalkthroughByUI() throws Exception {
        swaggerPage.openSwaggerUi();

        swaggerPage.executeList(null);
        assertEquals("200", swaggerPage.getResponseStatus("list"));

        String createdTitle = "UI automation testcase " + System.currentTimeMillis();
        swaggerPage.executeCreate("{\"title\":\"" + createdTitle + "\"}");
        assertEquals("200", swaggerPage.getResponseStatus("create"));

        JsonNode createResponse = objectMapper.readTree(swaggerPage.getResponseBody("create"));
        long testCaseId = createResponse.path("data").path("id").asLong();
        assertTrue(testCaseId > 0);
        assertEquals(createdTitle, createResponse.path("data").path("title").asText());

        swaggerPage.executeGetById(testCaseId);
        assertEquals("200", swaggerPage.getResponseStatus("getById"));
        JsonNode getResponse = objectMapper.readTree(swaggerPage.getResponseBody("getById"));
        assertEquals(testCaseId, getResponse.path("data").path("id").asLong());
        assertEquals(createdTitle, getResponse.path("data").path("title").asText());

        String updatedTitle = createdTitle + " updated";
        swaggerPage.executeUpdate(testCaseId, "{\"title\":\"" + updatedTitle + "\"}");
        assertEquals("200", swaggerPage.getResponseStatus("update"));
        JsonNode updateResponse = objectMapper.readTree(swaggerPage.getResponseBody("update"));
        assertEquals(testCaseId, updateResponse.path("data").path("id").asLong());
        assertEquals(updatedTitle, updateResponse.path("data").path("title").asText());

        swaggerPage.executeDelete(testCaseId);
        assertEquals("200", swaggerPage.getResponseStatus("delete"));
        JsonNode deleteResponse = objectMapper.readTree(swaggerPage.getResponseBody("delete"));
        assertEquals(200, deleteResponse.path("code").asInt());

        swaggerPage.takeScreenshot("swagger-ui-test");
        swaggerPage.pause(FINAL_REVIEW_MS);
    }

@Test
@DisplayName("删除测试用例 - 通过 Swagger UI 删除并验证响应码200")
void testDeleteTestCaseByUI() throws Exception {
    // 1. 打开 Swagger UI 页面
    swaggerPage.openSwaggerUi();
    
    // 2. 先创建一个测试用例（用于后续删除）
    String createdTitle = "DeleteTest_" + System.currentTimeMillis();
    swaggerPage.executeCreate("{\"title\":\"" + createdTitle + "\"}");
    assertEquals("200", swaggerPage.getResponseStatus("create"));
    
    // 3. 解析响应获取创建的用例ID
    JsonNode createResponse = objectMapper.readTree(swaggerPage.getResponseBody("create"));
    long testCaseId = createResponse.path("data").path("id").asLong();
    
    // 4. 执行删除操作
    swaggerPage.executeDelete(testCaseId);
    
    // 5. 验证删除响应码为200
    assertEquals("200", swaggerPage.getResponseStatus("delete"));
    
    // 6. 验证响应体中的code字段为200
    JsonNode deleteResponse = objectMapper.readTree(swaggerPage.getResponseBody("delete"));
    assertEquals(200, deleteResponse.path("code").asInt());
    
    // 7. 可选：验证删除后查询返回404或未找到
    swaggerPage.executeGetById(testCaseId);
    String getResponseBody = swaggerPage.getResponseBody("getById");
    JsonNode getResponse = objectMapper.readTree(getResponseBody);
    
    // 根据你的Controller逻辑，删除后查询应该返回"未找到数据"
    // 可能 code 不是200，或者 message 包含"未找到"
    System.out.println("删除后查询响应: " + getResponseBody);
    
    // 截图留证
    swaggerPage.takeScreenshot("delete-testcase-result");
    swaggerPage.pause(3000);
}

    @AfterEach
    void tearDown() {
        if (page != null) {
            page.close();
        }
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
