//package com.example.skinserver.redis;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.listener.PatternTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.util.concurrent.CountDownLatch;
//
///**
// * Created by chanson.cc on 2018/5/1.
// */
//@Controller
//@EnableAutoConfiguration
//public class RedisController {
//    @Autowired
//    StringRedisTemplate template;
//    @Autowired
//    CountDownLatch latch;
//
//
//    @PostMapping("/redis/send")
//    public @ResponseBody
//    String sendMessge(@RequestParam("msg") String message) {
//        sendMessgeInternal(message);
//        return "message sent:" + message;
//    }
//
//    @GetMapping("/redis")
//    public
//    String redisIndex(Model model) {
//        return "redis_index";
//    }
//
//
//    private void sendMessgeInternal(String message) {
//        template.convertAndSend("chat", "Hello from Redis!");
//        template.convertAndSend("chat", message);
//
//        try {
//            latch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Bean
//    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
//                                            MessageListenerAdapter listenerAdapter) {
//
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.addMessageListener(listenerAdapter, new PatternTopic("chat"));
//
//        return container;
//    }
//
//    @Bean
//    MessageListenerAdapter listenerAdapter(RedisReceiver receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }
//
//    @Bean
//    RedisReceiver redisReceiver(CountDownLatch latch) {
//        return new RedisReceiver(latch);
//    }
//
//    @Bean
//    CountDownLatch redisLatch() {
//        return new CountDownLatch(1);
//    }
//
//    @Bean
//    StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
//        return new StringRedisTemplate(connectionFactory);
//    }
//
//}
