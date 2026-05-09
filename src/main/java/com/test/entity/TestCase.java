package com.test.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestCase {

    private Long id;
    private String title;
    private String module;
    private String priority;
    private String precondition;
    private String steps;
    private String expectedResult;
    private String status;
    private String creator;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}