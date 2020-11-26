package com.haotang.server.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_ID = "ride_post_exchange";
    public static final String QUEUE_ID = "ride_post_queue";

    @Bean("ridePostExchange")
    public Exchange ridePostExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_ID).durable(true).build();
    }

    @Bean("ridePostQueue")
    public Queue ridePostQueue() {
        //return QueueBuilder.nonDurable(QUEUE_ID).build();
        return QueueBuilder.durable(QUEUE_ID).build();
    }

    @Bean
    public Binding ridePostQueueExchange(@Qualifier("ridePostQueue") Queue queue,
                                         @Qualifier("ridePostExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("post.#").noargs();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory jsaFactory(ConnectionFactory connectionFactory,
                                                           SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setConcurrentConsumers(50);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}
