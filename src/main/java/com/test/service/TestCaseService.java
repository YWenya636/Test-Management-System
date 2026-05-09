package com.test.service;

import com.test.entity.TestCase;

import java.util.List;

public interface TestCaseService {

    TestCase createTestCase(TestCase testCase);

    TestCase updateTestCase(Long id, TestCase testCase);

    boolean deleteTestCase(Long id);

    TestCase getTestCaseById(Long id);

    List<TestCase> getAllTestCases();

    List<TestCase> searchTestCases(String keyword);

    void deleteAllTestCases();
}