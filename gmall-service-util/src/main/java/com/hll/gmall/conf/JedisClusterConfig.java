package com.hll.gmall.conf;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class JedisClusterConfig {

    // 读取配置文件中 redis 的配置
    @Value("${spring.redis.cluster.nodes}")
    private String nodes;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private Integer timeout;

    /**
     * 注意：
     * 这里返回的 JedisCluster 是单例的，并且可以直接注入到其他类中去使用
     */
    @Bean
    public JedisCluster getJedisCluster() {
        String[] serverArray = nodes.split(","); // 获取服务器数组
        Set<HostAndPort> hostAndPorts = new HashSet<>();

        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            hostAndPorts.add(new HostAndPort(ipPortPair[0].trim(), Integer.parseInt(ipPortPair[1].trim())));
        }
        // soTimeout: 返回值的超时时间
        // maxAttempts：出现异常最大重试次数
        return new JedisCluster(hostAndPorts, timeout, 1000, 1, password, new GenericObjectPoolConfig());
    }

}
