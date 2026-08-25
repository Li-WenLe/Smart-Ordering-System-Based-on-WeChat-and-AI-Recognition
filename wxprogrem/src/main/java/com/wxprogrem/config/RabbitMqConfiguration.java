package com.wxprogrem.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class RabbitMqConfiguration implements RabbitListenerConfigurer {
    // 订单队列
    public static final String ORDER_QUEUE = "order_queue";

    // 订单交换机
    public static final String ORDER_EXCHANGE = "order_exchange";

    // 死信队列
    public static final String DLX_QUEUE = "dlx_queue";

    // 死信交换机
    public static final String DLX_EXCHANGE = "dlx_exchange";

    // 延时业务队列（发送延时消息用）
    public static final String DELAYED_BUSINESS_QUEUE = "delayed_business_queue";

    //延时业务队列交换机
    public static final String DELAYED_BUSINESS_EXCHANGE = "delayed_business_exchange";

    // 业务队列
    public static final String BUSINESS_QUEUE = "business_queue";

    // 业务队列交换机
    public static final String BUSINESS_EXCHANGE = "business_exchange";

    // 优惠券秒杀专用队列
    public static final String SICKILLVOUCHER_QUEUE = "sickillvoucher_queue";

    // 优惠券秒杀队列交换机
    public static final String SICKILLVOUCH_EXCHANGE = "sickillvoucher_exchange";

    //订单插入和库存扣减业务的队列交换机
    public static final String CREATORDERWITHSTOCK_BUSINESS_EXCHANGE = "createOrderWithStock_business_exchange";

    //订单插入和库存扣减业务的队列
    public static final String CREATORDERWITHSTOCK_BUSINESS_QUEUE = "createOrderWithStock_business_queue";

    // 优惠券专用队列
    @Bean
    public Queue sickillvoucherQueue() {
        return QueueBuilder.durable(SICKILLVOUCHER_QUEUE).build();
    }

    // 优惠券专用队列交换机
    @Bean
    public DirectExchange sickillvoucherExchange() {
        return new DirectExchange(SICKILLVOUCH_EXCHANGE);
    }

    /**
     * 优惠券队列绑定
     * 将 sickillvoucherQueue() 返回的 Queue 对象绑定到了 sickillvoucherExchange() 返回的
     * Exchange 对象，
     * 并指定了路由键 SICKILLVOUCHER_QUEUE
     * 
     * @return
     */
    @Bean
    public Binding sickillvoucherBinding() {
        log.info("正在创建绑定：交换器 {} -> 队列 {}，路由键 {}",
                SICKILLVOUCH_EXCHANGE, SICKILLVOUCHER_QUEUE, SICKILLVOUCHER_QUEUE);
        return BindingBuilder
                .bind(sickillvoucherQueue())
                .to(sickillvoucherExchange())
                .with(SICKILLVOUCHER_QUEUE);
    }

    // 死信队列
    @Bean
    public Queue dlxQueue() {
        return QueueBuilder.durable(DLX_QUEUE).build();
    }

    // 死信交换机
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    /**
     * 死信队列绑定
     * 把死信队列和死信队列交换机绑定起来路由键是DLX_QUEUE
     * 
     * @return
     */
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder
                .bind(dlxQueue())
                .to(dlxExchange())
                .with(DLX_QUEUE);
    }

    // 业务队列交换机
    @Bean
    public DirectExchange businessExchange() {
        return new DirectExchange(BUSINESS_EXCHANGE);
    }

    // 业务队列
    @Bean
    public Queue businessQueue() {
        return QueueBuilder.durable(BUSINESS_QUEUE).build();
    }

    /**
     * 业务队列绑定业务交换机
     * 
     * @return
     */
    @Bean
    public Binding BusinessBinding() {
        return BindingBuilder
                .bind(businessQueue())
                .to(businessExchange())
                .with(BUSINESS_QUEUE);
    }

    // 延时业务队列
    @Bean
    public Queue delayedBusinessQueue() {
        return QueueBuilder.durable(DELAYED_BUSINESS_QUEUE)
                .withArgument("x-message-ttl", 5000)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE) // 指定死信交换机
                .withArgument("x-dead-letter-routing-key", DLX_QUEUE) // 指定死信路由键
                .build();
    }

     // 延时交换机（延迟消息插件使用）
     @Bean
     public DirectExchange delayedBusinessExchange() {
         return new DirectExchange(DELAYED_BUSINESS_EXCHANGE);
     }

    /**
     * 延时业务队列绑定规则
     *
     * @return
     */
    @Bean
    public Binding DelayedBusinessBinding() {
        return BindingBuilder
                .bind(delayedBusinessQueue())
                .to(delayedBusinessExchange())
                .with(DELAYED_BUSINESS_QUEUE);
    }

    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true);
    }


    /**
     * createOrderWithStock订单插入和库存扣减业务队列
     *
     * @param
     */
    @Bean
    public DirectExchange createOrderWithStockBusinessExchange() {
        return new DirectExchange(CREATORDERWITHSTOCK_BUSINESS_EXCHANGE);
    }

    @Bean
    public Queue createOrderWithStockBusinessQueue() {
        return new Queue(CREATORDERWITHSTOCK_BUSINESS_QUEUE, true);
    }

    /**
     * 绑定规则
     * @param
     * @return
     */
    @Bean
    public Binding createOrderWithStockBusinessBinding() {
        return BindingBuilder
                .bind(createOrderWithStockBusinessQueue())
                .to(createOrderWithStockBusinessExchange())
                .with(CREATORDERWITHSTOCK_BUSINESS_QUEUE);
    }

    // 显式声明 AmqpAdmin，确保在上下文刷新时自动声明队列/交换机/绑定
    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(new MappingJackson2MessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter());
        return template;
    }

}