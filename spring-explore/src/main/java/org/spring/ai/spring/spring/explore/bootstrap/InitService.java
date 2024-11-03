package org.spring.ai.spring.spring.explore.bootstrap;

import org.spring.ai.spring.spring.explore.validator.UniqueUserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Service;

/**
 * @author ll
 * @since 2024-11-02 11:13
 */
@Service
@Slf4j
public class InitService implements ApplicationListener<ApplicationContextEvent> {
    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        ApplicationContext context = event.getApplicationContext();
        try {
            UniqueUserValidator bean = context.getBean(UniqueUserValidator.class);
            log.info("{}", bean.getClass());
        } catch (BeansException e) {
            log.warn("bean is not exist");
        }
    }
}
