package com.hll.gmall.conf;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

@Configuration
public class ActiveMQConfig {

    @Value("${spring.activemq.broker-url:disabled}")
    String brokerURL;

    @Value("${activemq.listener.enable:disabled}")
    String listenerEnable;

    @Bean(name = "connectionFactory")
    public ConnectionFactory getConnectionFactory() {
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();

        //加入连接池
        pooledConnectionFactory.setConnectionFactory(getActiveMQConnectionFactory());
        //出现异常时重新连接
        pooledConnectionFactory.setReconnectOnException(true);
        pooledConnectionFactory.setMaxConnections(5);
        pooledConnectionFactory.setExpiryTimeout(10000);

        return pooledConnectionFactory;
    }

    //定义一个消息监听器连接工厂，这里定义的是点对点模式的监听器连接工厂
    @Bean(name = "jmsQueueListener")
    public DefaultJmsListenerContainerFactory jmsQueueListenerContainerFactory() {
        if (!"true".equals(listenerEnable)) {
            return null;
        }

        DefaultJmsListenerContainerFactory listenerContainerFactory = new DefaultJmsListenerContainerFactory();

        listenerContainerFactory.setConnectionFactory(getActiveMQConnectionFactory());
        //设置并发数
        listenerContainerFactory.setConcurrency("5");
        //重连间隔时间
        listenerContainerFactory.setRecoveryInterval(5000L);
        listenerContainerFactory.setSessionTransacted(false);
        listenerContainerFactory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);

        return listenerContainerFactory;
    }

    private ActiveMQConnectionFactory getActiveMQConnectionFactory() {
        return new ActiveMQConnectionFactory(brokerURL);
    }
}
