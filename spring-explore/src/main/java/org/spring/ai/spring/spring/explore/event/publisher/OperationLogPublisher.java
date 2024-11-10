package org.spring.ai.spring.spring.explore.event.publisher;

import org.spring.ai.spring.spring.explore.event.OperationLogEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author ll
 * @since 2024-11-10 22:03
 */
@Component
public class OperationLogPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public OperationLogPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishEvent(String message) {
        applicationEventPublisher.publishEvent(new OperationLogEvent(this, message));
    }
}
