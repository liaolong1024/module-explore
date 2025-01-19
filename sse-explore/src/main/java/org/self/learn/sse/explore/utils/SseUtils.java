package org.self.learn.sse.explore.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ll
 * @since 2024-12-24 22:19
 */
@Slf4j
public class SseUtils {
    /**
     * 0为不过期
     */
    private static final long DEFAULT_TIME_OUT = 0L;

    private static Map<String, SseEmitter> conversationMap = new ConcurrentHashMap<>();

    public static SseEmitter getConnect(String conversationId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIME_OUT);
        try {
            // 重连事件
            sseEmitter.send(SseEmitter.event().reconnectTime(5000L).data("reconnect success"));

            // 定义连接超时事件Runnable: 删除会话ID
            sseEmitter.onTimeout(() -> SseUtils.timeout(conversationId));

            // 异常
            sseEmitter.onError((e) -> SseUtils.completion(conversationId));

            // 缓存会话
            conversationMap.put(conversationId, sseEmitter);

            log.info("connect success!");

        } catch (IOException e) {
            log.info("connect failed!, {}", e.getMessage());
        }
        return sseEmitter;
    }

    /**
     * 超时
     *
     * @param conversationId String 会话Id
     */
    public static void timeout(String conversationId) {
        // 日志
        log.error("sse连接超时 ==> 会话Id={}", conversationId);
        // 移除会话
        SseUtils.removeClientId(conversationId);
    }

    /**
     * 完成
     *
     * @param conversationId String 会话Id
     */
    public static void completion(String conversationId) {
        // 日志
        log.info("sse连接已断开 ==> 会话Id={}", conversationId);
        // 移除会话
        SseUtils.removeClientId(conversationId);
    }

    /**
     * 移除会话Id
     *
     * @param conversationId - 会话Id
     */
    public static void removeClientId(String conversationId) {
        // 不存在存在会话
        if (!SseUtils.isConversationIdExists(conversationId)) {
            return;
        }
        // 删除该会话
        conversationMap.remove(conversationId);
        // 日志
        log.info("移除会话成功 ==> 会话Id={}", conversationId);
    }

    /**
     * 获取是否存在会话
     *
     * @param conversationId - 会话Id
     */
    public static boolean isConversationIdExists(String conversationId) {
        return conversationMap.containsKey(conversationId);
    }
}
