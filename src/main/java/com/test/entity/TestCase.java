package com.test.entity;

import lombok.Data;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

@Data
public class TestCase {
    private Long id;
    
    @NotBlank(message = "标题不能为空")
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