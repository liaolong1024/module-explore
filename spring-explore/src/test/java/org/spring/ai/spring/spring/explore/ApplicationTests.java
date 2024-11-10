package org.spring.ai.spring.spring.explore;

import org.junit.jupiter.api.Test;
import org.spring.ai.spring.spring.explore.event.publisher.OperationLogPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {
    @Autowired
    private OperationLogPublisher operationLogPublisher;

    @Test
    void testOperationLogEvent() {
        operationLogPublisher.publishEvent("log event");
        System.out.println();
    }
}
