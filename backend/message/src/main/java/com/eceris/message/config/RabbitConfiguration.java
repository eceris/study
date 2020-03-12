package com.eceris.message.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfiguration {

    public static final String QUEUE_TOPIC_1 = "spring-boot-topic-1";
    public static final String QUEUE_TOPIC_2 = "spring-boot-topic-2";
    public static final String QUEUE_FANOUT_1 = "spring-boot-fanout-a";
    public static final String QUEUE_FANOUT_2 = "spring-boot-fanout-b";

    public static final String TOPIC_EXCHANGE_NAME = "topic-exchange";
    public static final String FANOUT_EXCHANGE_NAME = "fanout-exchange";

    private static final int MAX_TRY_COUNT = 3;
    private static final long INITIAL_INTERVAL = 3000;
    private static final double MULTIPLIER = 3;
    private static final long MAX_INTERVAL = 10000;

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(messageConverter());
        factory.setChannelTransacted(true);
        factory.setAdviceChain(RetryInterceptorBuilder
                .stateless()
                // 예외가 발생 했을 경우 몇번을 더 재시도 할 횟수
                .maxAttempts(MAX_TRY_COUNT)
                //리스너에서 장애발생시 .. 리커버리 핸들러
                .recoverer(new RabbitMqExceptionHandler())
                //재시도 횟수에 대한 옵션을 지정합니다. 3000, 3, 10000 인자값을 지정 했다면 3초 간격으로 3으로 곱해서 최대 10초 까지 재시도
                .backOffOptions(INITIAL_INTERVAL, MULTIPLIER, MAX_INTERVAL)
                .build());
        return factory;
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setChannelTransacted(true);
        rabbitTemplate.setReplyTimeout(60000);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE_NAME);
    }


    @Bean
    public Queue topicQueue1() {
        return new Queue(QUEUE_TOPIC_1, false);
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue(QUEUE_TOPIC_2, false);
    }


    @Bean
    public Queue fanoutQueue1() {
        return new Queue(QUEUE_FANOUT_1, false);
    }
    @Bean
    public Queue fanoutQueue2() {
        return new Queue(QUEUE_FANOUT_2, false);
    }



    @Bean
    public Binding topicBinding1(@Qualifier("topicQueue1") Queue topicQueue1, TopicExchange exchange) {
        return BindingBuilder.bind(topicQueue1).to(exchange).with("foo.bar");
    }

    @Bean
    public Binding topicBinding2(@Qualifier("topicQueue2") Queue topicQueue2, TopicExchange exchange) {
        return BindingBuilder.bind(topicQueue2).to(exchange).with("aa");
    }

    @Bean
    public Binding fanoutBinding1(FanoutExchange exchange) {
        return BindingBuilder.bind(fanoutQueue1()).to(exchange);
    }
    @Bean
    public Binding fanoutBinding2(FanoutExchange exchange) {
        return BindingBuilder.bind(fanoutQueue2()).to(exchange);
    }

}
