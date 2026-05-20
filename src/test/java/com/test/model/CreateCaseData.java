package com.test.model;

import com.test.entity.TestCase;
import lombok.Data;

@Data
public class CreateCaseData {
    private String title;
    private String module;
    private String priority;
    private int expectedStatus;

    public TestCase toTestCase() {
        TestCase testCase = new TestCase();
        testCase.setTitle(title);
        testCase.setModule(module);
        testCase.setPriority(priority);
        return testCase;
    }
}
