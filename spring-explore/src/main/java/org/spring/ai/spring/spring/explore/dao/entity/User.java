package org.spring.ai.spring.spring.explore.dao.entity;

import org.spring.ai.spring.spring.explore.validator.UniqueUser;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author ll
 * @since 2024-11-02 0:05
 */
@Setter
@Getter
public class User {
    @NotNull
    @UniqueUser(tableName = "t_user_dfjkdjfldsjfklajsdlfk")
    private String username;
}
