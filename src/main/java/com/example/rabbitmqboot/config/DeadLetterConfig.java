package com.example.rabbitmqboot.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeadLetterConfig {

    public static final String NORMAL_EXCHANGE = "normal-exchange";
    public static final String NORMAL_QUEUE = "normal-queue";
    public static final String NORMA_ROUTING_KEY = "normal.#";

    public static final String DEAD_EXCHANGE = "dead-exchange";
    public static final String DEAD_QUEUE = "dead-queue";
    public static final String DEAD_ROUTING_KEY = "dead.#";

    @Bean
    public Exchange normalExchange() {
        // channel.DeclareExchange
        return ExchangeBuilder.topicExchange(NORMAL_EXCHANGE).build();
    }

    @Bean
    public Queue normalQueue() {
        return QueueBuilder.durable(NORMAL_QUEUE)
                .deadLetterExchange(DEAD_EXCHANGE)
                .deadLetterRoutingKey("dead.#")
//                .ttl(10000) 给队列中全部消息设置生存时间
//                .maxLength(1) //给队列设置长度
                .build();
    }

    @Bean
    public Binding normalBinding(Exchange normalExchange, Queue normalQueue) {
        return BindingBuilder.bind(normalQueue).to(normalExchange).with(NORMA_ROUTING_KEY).noargs();
    }

    @Bean
    public Exchange deadExchange() {
        // channel.DeclareExchange
        return ExchangeBuilder.topicExchange(DEAD_EXCHANGE).build();
    }

    @Bean
    public Queue deadQueue() {
        return QueueBuilder.durable(DEAD_QUEUE).build();
    }

    @Bean
    public Binding deadBinding(Exchange deadExchange, Queue deadQueue) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(DEAD_ROUTING_KEY).noargs();
    }
}
