package com.hll.gmall.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Map;

@Component
public class RedisUtil {
    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 获取 JedisCluster
     *
     * @return RedissonClient
     */
    public JedisCluster getJedisCluster() {
        return this.jedisCluster;
    }

    /**
     * 设置缓存
     *
     * @param key   缓存key
     * @param value 缓存value
     */
    public void set(String key, String value) {
        jedisCluster.set(key, value);
    }

    /**
     * 设置缓存
     *
     * @param key   缓存key
     * @param hash 缓存hash
     */
    public void hSet(String key, Map<String, String> hash) {
        jedisCluster.hset(key, hash);
    }

    /**
     * 设置缓存
     * @param key   缓存key
     * @param field 缓存字段
     * @param value 缓存值
     */
    public void hSet(String key, String field, String value) {
        jedisCluster.hset(key, field, value);
    }

    /**
     * 设置缓存对象
     *
     * @param key        缓存key
     * @param obj        缓存value
     * @param expireTime 过期时间
     */
    public <T> void setObject(String key, T obj, int expireTime) {
        jedisCluster.setex(key, expireTime, JSON.toJSONString(obj));
    }

    /**
     * 获取指定key的缓存
     *
     * @param key---JSON.parseObject(value, User.class);
     */
    public String getObject(String key) {
        return jedisCluster.get(key);
    }

    /**
     * 获取指定key的缓存
     *
     * @param key
     * @param field
     */
    public String hGetObject(String key, String field) {
        return jedisCluster.hget(key, field);
    }

    /**
     * 获取指定key的缓存
     *
     * @param key
     */
    public Map<String, String> hGetAll(String key) {
        return jedisCluster.hgetAll(key);
    }

    /**
     * 判断当前key值 是否存在
     *
     * @param key
     */
    public boolean hasKey(String key) {
        return jedisCluster.exists(key);
    }

    /**
     * 设置缓存，并且自己指定过期时间
     *
     * @param key        key
     * @param value      值
     * @param expireTime 过期时间
     */
    public void setWithExpireTime(String key, String value, int expireTime) {
        jedisCluster.setex(key, expireTime, value);
    }

    /**
     * 获取指定key的缓存
     *
     * @param key key
     */
    public String get(String key) {
        return jedisCluster.get(key);
    }

    /**
     * 删除指定key的缓存
     *
     * @param key
     */
    public void delete(String key) {
        jedisCluster.del(key);
    }

    /**
     * 使用 lua 脚本
     *
     * @param script 脚本
     * @param keys keys
     * @param args args
     */
    public Object eval(String script, List<String> keys, List<String> args) {
        return jedisCluster.eval(script, keys, args);
    }

}