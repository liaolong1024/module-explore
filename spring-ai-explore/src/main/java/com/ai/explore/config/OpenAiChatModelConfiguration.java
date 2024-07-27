package com.ai.explore.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ll
 * @since  2024-07-25 22:43
 */
@Configuration
public class OpenAiChatModelConfiguration {
    @Bean
    public ChatClient gptRestTemplate(ChatClient.Builder builder) {
        return builder.build();
    }
}
