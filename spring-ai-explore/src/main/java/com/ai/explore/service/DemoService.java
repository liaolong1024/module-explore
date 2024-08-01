package com.ai.explore.service;

import com.ai.explore.entity.request.DemoRequest;
import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

/**
 * @author ll
 * @since 2024-07-27 23:12
 */
public interface DemoService {
    String ai(DemoRequest request);

    Flux<ChatResponse> stream(DemoRequest request);
}
