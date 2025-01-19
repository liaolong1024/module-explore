package org.self.learn.sse.explore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/sse")
public class SseDemoController {
    @GetMapping("/hello")
    public SseEmitter helloworld(HttpServletResponse response) {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("utf-8");
        SseEmitter sseEmitter = new SseEmitter();
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(1000L);
                    sseEmitter.send(SseEmitter.event().data(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
                }
            } catch (Exception e) {
                log.error("Error in SSE: {}", e.getMessage());
                sseEmitter.completeWithError(e);
            }
        }).start();
        return sseEmitter;
    }
}