package com.example.rabbitmqboot;

import com.example.rabbitmqboot.config.RabbitmqConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class PublisherTest {

    @Autowired
    public RabbitTemplate rabbitTemplate;

    @Test
    public void publish() {
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE, "big.black.dog", "message");
        System.out.println("消息发送成功");
    }


    @Test
    public void publishWithProps() {
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE, "big.black.dog", "messageWithProps", message -> {
            message.getMessageProperties().setCorrelationId("123");
            return message;
        });
        System.out.println("消息发送成功");
    }

    /**
     * 测试发布消息confirm
     * 需要配置 spring.rabbitmq.publisher-confirm-type=correlated
     */
    @Test
    public void publishWithConfirms() throws IOException {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (ack) {
                    System.out.println("消息已经送达到交换机！！");
                } else {
                    System.out.println("消息没有送达到Exchange，需要做一些补偿操作！！retry！！！");
                }
            }
        });
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE, "big.black.dog", "message");
        System.out.println("消息发送成功");

        System.in.read();
    }

    /**
     * 测试发布消息return机制
     * 需要配置 spring.rabbitmq.publisher-returns=true
     */
    @Test
    public void publishWithReturn() throws IOException {
        // 新版本用 setReturnsCallback ，老版本用setReturnCallback
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                String msg = new String(returned.getMessage().getBody());
                System.out.println("消息：" + msg + "路由队列失败！！做补救操作！！");
            }
        });
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE, "big.black.dog", "message");
        System.out.println("消息发送成功");

        System.in.read();
    }


    /**
     * 消息持久化 默认就是持久化的，发消息时候不需要设置这个
     */
    @Test
    public void publishWithBasicProperties() throws IOException {
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE, "big.black.dog", "message", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 设置消息的持久化！
                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return message;
            }
        });
        System.out.println("消息发送成功");
    }

}
