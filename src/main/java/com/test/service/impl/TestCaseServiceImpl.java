package com.test.service.impl;

import com.test.entity.TestCase;
import com.test.mapper.TestCaseMapper;
import com.test.service.TestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TestCaseServiceImpl implements TestCaseService {

    private final TestCaseMapper testCaseMapper;

    @Autowired
    public TestCaseServiceImpl(TestCaseMapper testCaseMapper) {
        this.testCaseMapper = testCaseMapper;
    }

    @Override
    public TestCase createTestCase(TestCase testCase) {
        LocalDateTime now = LocalDateTime.now();
        testCase.setCreateTime(now);
        testCase.setUpdateTime(now);
        testCaseMapper.insert(testCase);
        return testCase;
    }

    @Override
    public TestCase updateTestCase(Long id, TestCase testCase) {
        TestCase existing = testCaseMapper.selectById(id);
        if (existing == null) {
            return null;
        }
        testCase.setId(id);
        testCase.setUpdateTime(LocalDateTime.now());
        testCaseMapper.updateById(testCase);
        return testCaseMapper.selectById(id);
    }

    @Override
    public boolean deleteTestCase(Long id) {
        TestCase existing = testCaseMapper.selectById(id);
        if (existing == null) {
            return false;
        }
        return testCaseMapper.deleteById(id) > 0;
    }

    @Override
    public TestCase getTestCaseById(Long id) {
        return testCaseMapper.selectById(id);
    }

    @Override
    public List<TestCase> getAllTestCases() {
        return testCaseMapper.selectList();
    }

    @Override
    public List<TestCase> searchTestCases(String keyword) {
        return testCaseMapper.search(keyword);
    }

    @Override
    public void deleteAllTestCases() {
        testCaseMapper.deleteAll();
    }
}