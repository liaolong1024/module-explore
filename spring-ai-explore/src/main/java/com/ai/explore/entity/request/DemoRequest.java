package com.ai.explore.entity.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author ll
 * @since  2024-07-27 22:35
 */
@Setter
@Getter
public class DemoRequest {
    @NotBlank(message = "message cannot be blank")
    private String message;
}
