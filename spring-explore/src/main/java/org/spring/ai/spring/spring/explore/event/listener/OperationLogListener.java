package org.spring.ai.spring.spring.explore.event.listener;

import org.spring.ai.spring.spring.explore.event.OperationLogEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author ll
 * @since 2024-11-10 22:01
 */
@Component
public class OperationLogListener implements ApplicationListener<OperationLogEvent> {
    @Async
    @Override
    public void onApplicationEvent(OperationLogEvent event) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("save log");
    }
}
