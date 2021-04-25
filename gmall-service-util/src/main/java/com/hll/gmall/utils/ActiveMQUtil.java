package com.hll.gmall.utils;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;

@Component
public class ActiveMQUtil {
    @Autowired
    private static ConnectionFactory connectionFactory;

    @Autowired
    private static DefaultJmsListenerContainerFactory listenerContainerFactory;

    public static ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public static DefaultJmsListenerContainerFactory jmsQueueListenerContainerFactory() {
        return listenerContainerFactory;
    }
}
