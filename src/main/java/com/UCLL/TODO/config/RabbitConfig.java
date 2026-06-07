package com.UCLL.TODO.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;



@Configuration
public class RabbitConfig {

    public static final String AUDIT_QUEUE = "audit-queue";

    @Bean
    public Queue queue() {
        return new Queue(AUDIT_QUEUE);
    }

    // Deze bean zorgt ervoor dat de messages geconverteerd worden naar JSON
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
