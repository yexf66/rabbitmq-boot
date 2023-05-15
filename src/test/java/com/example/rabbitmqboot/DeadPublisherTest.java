package com.example.rabbitmqboot;

import com.example.rabbitmqboot.config.DeadLetterConfig;
import com.example.rabbitmqboot.config.RabbitmqConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DeadPublisherTest {

    @Autowired
    public RabbitTemplate rabbitTemplate;

    @Test
    public void publish() {
        rabbitTemplate.convertAndSend(DeadLetterConfig.NORMAL_EXCHANGE, "normal.abc", "message");
        System.out.println("消息发送成功");
    }

    /**
     * 测试这里需要注释 DeadListener
     */
    @Test
    public void publishExpired() {
        rabbitTemplate.convertAndSend(DeadLetterConfig.NORMAL_EXCHANGE, "normal.abc", "deadletter expired", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        });
        System.out.println("消息发送成功");
    }





}
