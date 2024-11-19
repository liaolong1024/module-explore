package org.spring.ai.spring.spring.explore.controller;

import org.apache.commons.text.StringEscapeUtils;
import org.spring.ai.spring.spring.explore.dao.entity.User;
import org.spring.ai.spring.spring.explore.dao.entity.User2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author ll
 * @since 2024-11-02 0:03
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/get")
    public String getUser(@RequestBody @Valid @Validated User user) {
        return StringEscapeUtils.unescapeJava(user.getUsername());
    }

    @GetMapping("/get2")
    public String getUser2(@RequestBody @Valid @Validated User2 user) {
        return user.getUsername();
    }
}
