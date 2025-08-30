package com.aa.controller;

import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ll
 * @since 2025-08-11 20:53
 */
@RestController
public class UserController {
    private Logger logger;
    @RequestMapping(path = "/aaa", method = RequestMethod.GET)
    public String s() {
        return "hi";
    }

    public static void main(String[] args) {
        System.out.println("abcd\n\\");
    }
}
