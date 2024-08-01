package com.ai.explore.controller;

import com.ai.explore.entity.request.DemoRequest;
import com.ai.explore.service.DemoService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

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
    @PostMapping("/ai/generate")
    public String ai(@RequestBody @Valid DemoRequest request) {
        return demoService.ai(request);
    }

    @PostMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestBody @Valid DemoRequest request) {
        return demoService.stream(request);
    }
}
