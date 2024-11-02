package com.spring.ai.es.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ll
 * @since 2024-10-24 22:54
 */
@RestController
@RequestMapping("/es")
public class EsController {
    @RequestMapping(path = "/ecommerce", method = RequestMethod.POST)
    public String log(String ecommerce) {
        return ecommerce;
    }
}
