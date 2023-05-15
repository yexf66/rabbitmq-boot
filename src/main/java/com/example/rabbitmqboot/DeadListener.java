//package com.example.rabbitmqboot;
//
//import com.example.rabbitmqboot.config.DeadLetterConfig;
//import com.rabbitmq.client.Channel;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class DeadListener {
//
//    @RabbitListener(queues = DeadLetterConfig.NORMAL_QUEUE)
//    public void consume(String msg, Channel channel, Message message) throws IOException {
//        System.out.println("NORMAL_QUEUE 队列的消息为：" + msg);
//        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
//    }
//}
