package org.spring.ai.spring.spring.explore.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author ll
 * @since 2024-11-10 21:58
 */
public class OperationLogEvent extends ApplicationEvent {
    private String message;

    public OperationLogEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
