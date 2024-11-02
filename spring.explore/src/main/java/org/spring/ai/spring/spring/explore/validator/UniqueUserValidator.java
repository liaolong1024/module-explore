package org.spring.ai.spring.spring.explore.validator;

import org.spring.ai.spring.spring.explore.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author ll
 * @since 2024-11-02 11:15
 */
public class UniqueUserValidator implements ConstraintValidator<UniqueUser, String> {
    @Autowired
    private UserController userController;

    @Autowired
    private LocalValidatorFactoryBean localValidatorFactoryBean;

    private String tableName;

    @Override
    public void initialize(UniqueUser a) {
        tableName = a.tableName();
    }

    @Override
    public boolean isValid(String user, ConstraintValidatorContext constraintValidatorContext) {
        System.out.println(this.getClass());
        return true;
    }
}
