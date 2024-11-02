package com.spring.ai.spring.spring.explore.dao.entity;

import com.spring.ai.spring.spring.explore.validator.UniqueUser;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author ll
 * @since 2024-11-02 0:05
 */
@Setter
@Getter
public class User2 {
    @NotNull
    @UniqueUser(tableName = "t_user_2")
    private String username;
}
