package com.hll.gmall.utils;

import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonUtil {
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 获取RedissonClient
     *
     * @return RedissonClient
     */
    public RedissonClient getRedissonClient() {
        return this.redissonClient;
    }

    /**
     * 加锁
     *
     * @return
     */
    public void lock(String lockKey) {
        redissonClient.getLock(lockKey).lock();
    }

    /**
     * 释放锁
     */
    public void unLock(String lockKey) {
        redissonClient.getLock(lockKey).unlock();
    }

    /**
     * 带超时的加锁
     *
     * @param lockKey
     * @param tomeout 秒为单位
     */
    public void lock(String lockKey, Long tomeout) {
        redissonClient.getLock(lockKey).lock(tomeout, TimeUnit.SECONDS);
    }

    /**
     * 带超时的加锁
     *
     * @param lockKey
     * @param unit    时间单位
     * @param tomeout
     */
    public void lock(String lockKey, TimeUnit unit, Long tomeout) {
        redissonClient.getLock(lockKey).lock(tomeout, unit);
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @return
     */
    public boolean tryLock(String lockKey) {
        return redissonClient.getLock(lockKey).tryLock();
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param timeout 尝试等待多少秒时间
     * @return boolean
     * @throws InterruptedException InterruptedException
     */
    public boolean tryLock(String lockKey, Long timeout) throws InterruptedException {
        return redissonClient.getLock(lockKey).tryLock(timeout, TimeUnit.SECONDS);
    }

    /**
     * @param lockKey   lockKey
     * @param unit      时间单位
     * @param waitTime  最多等待多久时间
     * @param leaseTime 上锁后多久释放
     * @return boolean
     */
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) throws InterruptedException {
        return redissonClient.getLock(lockKey).tryLock(waitTime, leaseTime, unit);
    }

    /**
     * 关闭Redisson客户端连接
     */
    public void closeRedisson() {
        redissonClient.shutdown();
    }

    /**
     * 获取字符串对象
     *
     * @param objectName
     * @return
     */
    public <T> RBucket<T> getRBucket(String objectName) {
        return redissonClient.getBucket(objectName);
    }

    /**
     * 获取Map对象
     *
     * @param objectName
     * @return
     */
    public <K, V> RMap<K, V> getRMap(String objectName) {
        return redissonClient.getMap(objectName);
    }

    /**
     * 获取有序集合
     *
     * @param objectName
     * @return
     */
    public <V> RSortedSet<V> getRSortedSet(String objectName) {
        return redissonClient.getSortedSet(objectName);
    }

    /**
     * 获取集合
     *
     * @param objectName
     * @return
     */
    public <V> RSet<V> getRSet(String objectName) {
        return redissonClient.getSet(objectName);
    }

    /**
     * 获取列表
     *
     * @param objectName
     * @return
     */
    public <V> RList<V> getRList(String objectName) {
        return redissonClient.getList(objectName);
    }

    /**
     * 获取队列
     *
     * @param objectName
     * @return
     */
    public <V> RQueue<V> getRQueue(String objectName) {
        return redissonClient.getQueue(objectName);
    }

    /**
     * 获取双端队列
     *
     * @param objectName
     * @return
     */
    public <V> RDeque<V> getRDeque(String objectName) {
        return redissonClient.getDeque(objectName);
    }

    /**
     * 获取锁
     *
     * @param objectName
     * @return
     */
    public RLock getRLock(String objectName) {
        return redissonClient.getLock(objectName);
    }

    /**
     * 获取原子数
     *
     * @param objectName
     * @return
     */
    public RAtomicLong getRAtomicLong(String objectName) {
        return redissonClient.getAtomicLong(objectName);
    }

    /**
     * 获取记数锁
     *
     * @param objectName
     * @return
     */
    public RCountDownLatch getRCountDownLatch(RedissonClient redisson, String objectName) {
        return redissonClient.getCountDownLatch(objectName);
    }

    /**
     * 获取消息的Topic
     *
     * @param objectName
     * @return
     */
    public RTopic getRTopic(String objectName) {
        return redissonClient.getTopic(objectName);
    }

}