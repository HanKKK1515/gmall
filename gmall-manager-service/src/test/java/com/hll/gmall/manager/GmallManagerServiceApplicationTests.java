package com.hll.gmall.manager;

import com.hll.gmall.utils.RedisUtil;
import com.hll.gmall.utils.RedissonUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @RunWith 是一个运行器，实质上是一个接口，是用来扩展 Junit 的 Test Runner 的。
 * Suite , Parameterized 以及 SpringTest 都是 Test Runner ,他们都是 org.junit.runner.Runner 的子类。
 * @RunWith(JUnit4.class) 指用 JUnit4 来运行
 * @RunWith(SpringJUnit4ClassRunner.class) 让测试运行于 Spring 测试环境。
 * 在测试开始的时候自动创建 Spring 的应用上下文。
 * @RunWith(Suite.class) 是一套测试集合
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class GmallManagerServiceApplicationTests {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedissonUtil redissonUtil;

    @Test
    void textJedis() {
        String k = redisUtil.get("k");
        if (StringUtils.isBlank(k)) {
            k = "1";
        } else {
            k = Integer.parseInt(k) + 1 + "";
        }
        redisUtil.set("k", k);
        System.out.println("--> " + k);
    }

    @Test
    void testRedisson() {
        RLock lock = redissonUtil.getRLock("aaa_1212_lock");
        try {
            boolean bs = lock.tryLock(5, 6, TimeUnit.SECONDS);
            if (bs) {
                // 业务代码
                System.out.println("进入业务代码: " + "aaa");

                lock.unlock();
            } else {
                Thread.sleep(300);
            }
        } catch (Exception e) {
            System.out.println("异常");
            lock.unlock();
        }
    }

}
