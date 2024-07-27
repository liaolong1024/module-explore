package com.ai.explore.controller;

import com.ai.explore.entity.request.DemoRequest;
import com.ai.explore.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ll
 * @since 2024-07-25 22:38
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    private DemoService demoService;

    /**
     * 测试
     *
     * @param request 请求体
     * @return 响应
     */
    @PostMapping("/ai")
    public String ai(@RequestBody DemoRequest request) {
        return demoService.ai(request);
    }
}
