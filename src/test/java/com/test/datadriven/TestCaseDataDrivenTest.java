package com.test.datadriven;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.assertion.TestCaseAssert;
import com.test.config.GlobalExceptionHandler;
import com.test.controller.TestCaseController;
import com.test.entity.TestCase;
import com.test.mapper.TestCaseMapper;
import com.test.model.CreateCaseData;
import com.test.service.TestCaseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestCaseController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("Data-driven testcase creation tests")
class TestCaseDataDrivenTest {

    private static final AtomicLong ID_SEQUENCE = new AtomicLong(1000);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TestCaseService testCaseService;

    @MockBean
    private TestCaseMapper testCaseMapper;

    @Test
    @DisplayName("JSON file drives testcase creation scenarios")
    void shouldCreateTestCaseFromJson() throws Exception {
        stubCreateService();

        InputStream inputStream = getClass().getResourceAsStream("/testdata/create_cases.json");
        List<CreateCaseData> testCases = objectMapper.readValue(
                inputStream,
                new TypeReference<List<CreateCaseData>>() {
                });

        for (CreateCaseData testCase : testCases) {
            assertCreateScenario(testCase);
        }
    }

    @ParameterizedTest(name = "[{index}] {0} / {1} / {2} -> {3}")
    @CsvFileSource(resources = "/testdata/create_cases.csv", numLinesToSkip = 1)
    @DisplayName("CSV file drives testcase creation scenarios")
    void shouldCreateTestCaseFromCsv(String title, String module, String priority, int expectedStatus)
            throws Exception {
        stubCreateService();

        CreateCaseData testCase = new CreateCaseData();
        testCase.setTitle(title);
        testCase.setModule(module);
        testCase.setPriority(priority);
        testCase.setExpectedStatus(expectedStatus);

        assertCreateScenario(testCase);
    }

    @Test
    @DisplayName("Custom AssertJ assertion validates created testcase business rules")
    void shouldValidateCreatedCaseWithCustomAssertion() {
        TestCase created = new TestCase();
        created.setId(1L);
        created.setTitle("Login succeeds with valid account");
        created.setModule("User Center");
        created.setPriority("P0");
        created.setStatus("DRAFT");
        created.setCreateTime(LocalDateTime.now());
        created.setUpdateTime(LocalDateTime.now());

        TestCaseAssert.assertThat(created)
                .isCreatedSuccessfully()
                .hasBusinessFields("Login succeeds with valid account", "User Center", "P0");
    }

    private void assertCreateScenario(CreateCaseData data) throws Exception {
        var action = mockMvc.perform(post("/api/testcases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data.toTestCase())));

        if (data.getExpectedStatus() == 200) {
            action.andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.title").value(data.getTitle()))
                    .andExpect(jsonPath("$.data.module").value(data.getModule()))
                    .andExpect(jsonPath("$.data.priority").value(data.getPriority()))
                    .andExpect(jsonPath("$.data.status").value("DRAFT"));
        } else {
            action.andExpect(status().is(data.getExpectedStatus()))
                    .andExpect(jsonPath("$.code").value(data.getExpectedStatus()));
        }
    }

    private void stubCreateService() {
        when(testCaseService.createTestCase(any(TestCase.class))).thenAnswer(invocation -> {
            TestCase input = invocation.getArgument(0);
            input.setId(ID_SEQUENCE.incrementAndGet());
            input.setStatus("DRAFT");
            input.setCreateTime(LocalDateTime.now());
            input.setUpdateTime(LocalDateTime.now());
            return input;
        });
    }
}
