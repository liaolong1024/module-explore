package com.ai.explore.service.impl;

import com.ai.explore.entity.request.DemoRequest;
import com.ai.explore.service.DemoService;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ll
 * @since 2024-07-27 23:12
 */
@Service
public class DemoServiceImpl implements DemoService {
    @Autowired
    private OpenAiChatModel openAiChatModel;

    @Override
    public String ai(DemoRequest request) {
        return openAiChatModel.call(request.getMessage());
    }
}
