package com.hll.gmall.conf;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    // 读取配置文件中 redis 的配置
    @Value("${spring.redis.cluster.nodes}")
    private String nodes;

    @Value("${spring.redis.timeout}")
    private Integer timeout;

    @Value("${spring.redis.password}")
    private String password;

    /**
     * 注意：
     * 这里返回的 JedisCluster 是单例的，并且可以直接注入到其他类中去使用
     */
    @Bean
    public RedissonClient getRedissonClient() {
        String[] serverArray = nodes.split(",");
        for (int i = 0; i < serverArray.length; i++) {
            serverArray[i] = "redis://" + serverArray[i];
        }


        Config config = new Config();
        config.useClusterServers().addNodeAddress(serverArray).setPassword(password).setTimeout(timeout);

        return Redisson.create(config);
    }

}
