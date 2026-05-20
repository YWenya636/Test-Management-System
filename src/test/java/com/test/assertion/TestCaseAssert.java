package com.test.assertion;

import com.test.entity.TestCase;
import org.assertj.core.api.AbstractAssert;

public class TestCaseAssert extends AbstractAssert<TestCaseAssert, TestCase> {

    private TestCaseAssert(TestCase actual) {
        super(actual, TestCaseAssert.class);
    }

    public static TestCaseAssert assertThat(TestCase actual) {
        return new TestCaseAssert(actual);
    }

    public TestCaseAssert isCreatedSuccessfully() {
        isNotNull();
        if (actual.getId() == null || actual.getId() <= 0) {
            failWithMessage("Expected id to be a positive number, but was <%s>", actual.getId());
        }
        if (actual.getCreateTime() == null) {
            failWithMessage("Expected createTime not null, but was null");
        }
        if (actual.getUpdateTime() == null) {
            failWithMessage("Expected updateTime not null, but was null");
        }
        if (!"DRAFT".equals(actual.getStatus())) {
            failWithMessage("Expected status DRAFT, but was <%s>", actual.getStatus());
        }
        return this;
    }

    public TestCaseAssert hasBusinessFields(String title, String module, String priority) {
        isNotNull();
        if (!title.equals(actual.getTitle())) {
            failWithMessage("Expected title <%s>, but was <%s>", title, actual.getTitle());
        }
        if (!module.equals(actual.getModule())) {
            failWithMessage("Expected module <%s>, but was <%s>", module, actual.getModule());
        }
        if (!priority.equals(actual.getPriority())) {
            failWithMessage("Expected priority <%s>, but was <%s>", priority, actual.getPriority());
        }
        return this;
    }
}
