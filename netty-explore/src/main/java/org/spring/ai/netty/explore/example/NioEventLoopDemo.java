package org.spring.ai.netty.explore.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author ll
 * @since 2024-09-28 15:24
 */
public class NioEventLoopDemo {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup(2);
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(group);

    }
}
