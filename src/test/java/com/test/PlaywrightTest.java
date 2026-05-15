
package com.test;

import com.microsoft.playwright.*;

public class PlaywrightTest {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            // 启动Chromium浏览器
            Browser browser = playwright.chromium().launch();
            // 打开新页面访问百度（修正了多余的url:语法）
            Page page = browser.newPage();
            page.navigate("https://www.baidu.com");
            // 打印页面标题
            System.out.println("页面标题：" + page.title());
            browser.close();
        }
    }
}