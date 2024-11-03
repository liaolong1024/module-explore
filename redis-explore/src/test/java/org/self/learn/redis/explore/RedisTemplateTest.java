package org.self.learn.redis.explore;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ll
 * @since 2024-11-03 20:37
 */
@SpringBootTest
public class RedisTemplateTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisTemplateTest.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Test
    void testCommonCommand() {
        String key = "string::test::01";
        // set key
        stringRedisTemplate.opsForValue().set(key, "a");
        // get key
        LOGGER.info(stringRedisTemplate.opsForValue().get(key));
        // delete key
        stringRedisTemplate.delete(key);
        LOGGER.info(stringRedisTemplate.opsForValue().get(key));
        // exists key
        LOGGER.info("{}", stringRedisTemplate.hasKey(key));
        // expire key || pexpire key
        stringRedisTemplate.expire(key, 10, TimeUnit.SECONDS);
        // expireat key || pexpireat key
        stringRedisTemplate.expireAt(key, new Date());
        // keys
        Set<String> matchKeys = stringRedisTemplate.keys(key);
        // move key db
        Boolean moved = stringRedisTemplate.move(key, 0);
        // ttl key (秒)
        Long expire = stringRedisTemplate.getExpire(key);
        // rename key newKey (key不存在时报错， newKey存在时会被key的值覆盖)
        stringRedisTemplate.rename(key, key + "_new");
        // renamenx key newKey (key不存在时报错， newKey存在时什么都不做)
        stringRedisTemplate.renameIfAbsent(key, key + "_new_new");
        // type key
        DataType type = stringRedisTemplate.type(key);
    }

    @Test
    void testStringOperation() {
        String key = "string::test::02";
        // set key value
        stringRedisTemplate.opsForValue().set(key, "value");
        // get key
        stringRedisTemplate.opsForValue().get(key);
        // getrange key start end (start end都是包含的)
        String s = stringRedisTemplate.opsForValue().get(key, 0, 1);
        // getset key newvalue (设置新值，返回旧值)
        stringRedisTemplate.opsForValue().getAndSet(key, "newValue");
        // getbit key offset
        stringRedisTemplate.opsForValue().set(key, "a");
        StringBuilder bitBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int bit = Boolean.TRUE.equals(stringRedisTemplate.opsForValue().getBit(key, i)) ? 1 : 0;
            bitBuilder.append(bit);
        }
        LOGGER.info(bitBuilder.toString());
        LOGGER.info("{}", Integer.valueOf(bitBuilder.toString(), 2));

        // mget key1 key2 ...
        List<String> strings = stringRedisTemplate.opsForValue().multiGet(Lists.newArrayList("a", "b"));

        // setbit key offset bit(0或1)
        stringRedisTemplate.opsForValue().setBit(key, 0, true);

        // setex key seconds value
        stringRedisTemplate.opsForValue().set(key, "nnn", 10, TimeUnit.SECONDS);

        // setnx key value
        stringRedisTemplate.opsForValue().setIfAbsent(key, "aaa");

        // set key value EX 10 NX
        stringRedisTemplate.opsForValue().setIfAbsent(key, "aaa", 1, TimeUnit.SECONDS);

        // setrange key offset subnewvalue
        stringRedisTemplate.opsForValue().set(key, "a", 1);

        // strlen key
        stringRedisTemplate.opsForValue().size(key);

        // mset key1 value1 key2 value2 ...
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("a", "1");
                put("b", "2");
            }
        };
        stringRedisTemplate.opsForValue().multiSet(map);

        // append key value
        stringRedisTemplate.opsForValue().append(key, "a");

        redisTemplate.opsForValue().set(key, 1);
        // incr key
        redisTemplate.opsForValue().increment(key);
        // decr key
        redisTemplate.opsForValue().decrement(key);
        // decr key delta
        redisTemplate.opsForValue().decrement(key, 1);

        System.out.println();
    }
}
