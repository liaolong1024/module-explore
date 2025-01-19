package org.self.learn.sse.explore.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ll
 * @since 2024-12-23 23:00
 */
@Setter
@Getter
@AllArgsConstructor
public class ChatMessage {
    private String role;

    private String content;
}
