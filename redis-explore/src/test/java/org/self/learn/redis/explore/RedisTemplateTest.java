package org.self.learn.redis.explore;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.core.*;

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
        String key = "string:test:01";
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
        String key = "string:test:02";
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

    @Test
    void testHashOperation() {
        String key = "hash:test:01";
        // hset key field1 value1
        stringRedisTemplate.opsForHash().put(key, "a", "b");
        // hget key field1
        Object a = stringRedisTemplate.opsForHash().get(key, "a");
        // hdel key field1
        stringRedisTemplate.opsForHash().delete(key, "a");
        // hexists key field1
        stringRedisTemplate.opsForHash().hasKey(key, "a");
        // hgetall key
        stringRedisTemplate.opsForHash().entries(key);
        // hincrby key field1 increment
        stringRedisTemplate.opsForHash().increment(key, "a", 1);
        // hkeys key
        stringRedisTemplate.opsForHash().keys(key);
        // hvals key
        stringRedisTemplate.opsForHash().values(key);
        // hlen key
        stringRedisTemplate.opsForHash().size(key);
        // hmget key field1 field2
        stringRedisTemplate.opsForHash().multiGet(key, Lists.newArrayList("a"));
        // hmset key field1 value1 field2 value2
        stringRedisTemplate.opsForHash().putAll(key, new HashMap<String, String>() {
            {
                put("a", "a");
            }
        });
        // hsetnx key field value
        stringRedisTemplate.opsForHash().putIfAbsent(key, "d", "d");
        // hscan key cursor [MATCH pattern] [COUNT count]
        try (Cursor<Map.Entry<Object, Object>> entryCursor = stringRedisTemplate.opsForHash().scan(key, ScanOptions.scanOptions().count(1).build())) {
            while (entryCursor.hasNext()) {
                Map.Entry<Object, Object> keyValue = entryCursor.next();
                System.out.println(keyValue.getKey());
            }
        }
    }

    @Test
    void testListOperation() {
        String key = "list:test:01";
        // lpush key 1
        stringRedisTemplate.opsForList().leftPush(key, "1");
        stringRedisTemplate.opsForList().leftPush(key, "2");

        // lpush key val1 val2
        stringRedisTemplate.opsForList().leftPushAll(key, "1", "2");

        // rpop key
        stringRedisTemplate.opsForList().rightPop(key);

        // blpop key timeout
        stringRedisTemplate.opsForList().leftPop(key, 10, TimeUnit.SECONDS);

        // brpoplpush source dest timeout
        stringRedisTemplate.opsForList().rightPopAndLeftPush(key, key, 10, TimeUnit.SECONDS);

        // lindex key index
        String valueOfIdx0 = stringRedisTemplate.opsForList().index(key, 0);

        // linsert key before|after pivot val
        redisTemplate.execute((RedisCallback<Object>) connection -> connection.listCommands().lInsert(key.getBytes(), RedisListCommands.Position.AFTER, "2".getBytes(), "3".getBytes()));

        // llen key
        stringRedisTemplate.opsForList().size(key);

        // lpushx key val
        stringRedisTemplate.opsForList().leftPushIfPresent(key, "e");

        // lrange key start end
        stringRedisTemplate.opsForList().range(key, 0, 2);

        // lrem key count val
        stringRedisTemplate.opsForList().remove(key, 0, 2);

        // lset key index val
        stringRedisTemplate.opsForList().set(key, 0, "ee");

        // ltrim key start end
        stringRedisTemplate.opsForList().trim(key, 0, 5);

        System.out.println();
    }

    @Test
    void testSetOperation() {
        String key = "set:test:01";
        String key2 = "set:test:02";
        // sadd key member
        stringRedisTemplate.opsForSet().add(key, "a");
        stringRedisTemplate.opsForSet().add(key, "b");
        stringRedisTemplate.opsForSet().add(key2, "b");

        // smembers key
        stringRedisTemplate.opsForSet().members(key);

        // scard key
        stringRedisTemplate.opsForSet().size(key);

        // sdiff key1 key2
        stringRedisTemplate.opsForSet().difference(Lists.newArrayList(key, key2));

        // sdiffstore destKey key1 key2 ..
        stringRedisTemplate.opsForSet().differenceAndStore(key, key2, "newKey");

        // sismember key val
        stringRedisTemplate.opsForSet().isMember(key, "a");

        // smove sourcekey destkey
        stringRedisTemplate.opsForSet().move(key, key2, "d");

        // spop key
        stringRedisTemplate.opsForSet().pop(key);

        // srandommember key
        stringRedisTemplate.opsForSet().randomMember(key);

        // srem key val
        stringRedisTemplate.opsForSet().remove(key, "a");

        // sunion key val
        stringRedisTemplate.opsForSet().union(key, key2);

        // sscan key cursor
        stringRedisTemplate.opsForSet().scan(key, ScanOptions.scanOptions().build());

    }

    @Test
    void testSortedSetOperation() {
        String key = "zset:test:01";
        // zadd key score member
        stringRedisTemplate.opsForZSet().add(key, "a", 1);

        // scard key
        stringRedisTemplate.opsForZSet().size(key);

        // zcount key minS maxS
        stringRedisTemplate.opsForZSet().count(key, 0, 100);

        // zincr key increment member
        stringRedisTemplate.opsForZSet().incrementScore(key, "a", 10);
    }

    @Test
    void testHyperLogLog() {
        String key = "hyperloglog:test:01";

        // pfadd key elem
        stringRedisTemplate.opsForHyperLogLog().add(key, "a", "b", "a", "c");

        // pfcount key
        stringRedisTemplate.opsForHyperLogLog().size(key);

        // pfmerge dest source1 source2
        stringRedisTemplate.opsForHyperLogLog().union("dest", key);
    }

    @Test
    void testPubSub() {
        String key = "pubsub:test:01";
        redisTemplate.convertAndSend("chat", "hello");
    }

    /**
     * redis事务无回滚机制
     * 1. 语法错误时，事务里所有命令都不会执行
     * 2. 运行时错误，比如对list类型数据做hash类型数据的操作，正确的命令会执行
     * 3. watch key： 监控key的变化，当key被修改后，multi事务中的修改或删除(因为过期而删除不属于此类)等命令不执行
     */
    @Test
    void testTransaction() {
        String key = "a";
        stringRedisTemplate.multi();
        stringRedisTemplate.opsForValue().get(key);
        stringRedisTemplate.opsForValue().set(key, "a");
        stringRedisTemplate.exec();

        // watch
        stringRedisTemplate.watch(key);
        stringRedisTemplate.opsForValue().set(key, "b");
        stringRedisTemplate.multi();
        stringRedisTemplate.opsForValue().get(key);
        // 该命令不执行
        stringRedisTemplate.opsForValue().set(key, "a");
        stringRedisTemplate.exec();
        stringRedisTemplate.unwatch();
    }
}
