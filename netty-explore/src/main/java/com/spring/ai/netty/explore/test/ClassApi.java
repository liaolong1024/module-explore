package com.spring.ai.netty.explore.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author ll
 * @since 2024-08-18 15:59
 */
@Slf4j
public class ClassApi {
    public static void main(String[] args) throws Exception {
        ClassPathResource resource = new ClassPathResource("data.json");

        try (FileChannel channel = new FileInputStream(resource.getFile()).getChannel()) {
            ByteBuffer buf = ByteBuffer.allocate(100);
            int read = channel.read(buf);

            // 切换为读模式
            buf.flip();
            StringBuilder builder = new StringBuilder();
            while (buf.hasRemaining()) {
                byte b = buf.get();
                builder.append((char) b);
            }
            // 切换为写模式
            buf.clear();
            log.info(builder.toString());
        }
    }
}
