package com.eceris.message.listener.topic;

import com.eceris.message.config.RabbitConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CarMessageListener {

    /** annotation based listener **/
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "dodge_cars_queue", durable = "true"),
            exchange = @Exchange(value = RabbitConfiguration.TOPIC_EXCHANGE_NAME, ignoreDeclarationExceptions = "true"),
            key = "dodge.cars.*")
    )
    public void dodge(String car) {
        log.info("dodge : {}", car);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ford_cars_queue", durable = "true"),
            exchange = @Exchange(value = RabbitConfiguration.TOPIC_EXCHANGE_NAME, ignoreDeclarationExceptions = "true"),
            key = "ford.cars.*")
    )
    public void ford(String car) {
        log.info("ford : {}", car);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "all_cars_queue", durable = "true"),
            exchange = @Exchange(value = RabbitConfiguration.TOPIC_EXCHANGE_NAME, ignoreDeclarationExceptions = "true"),
            key = "*.cars.#")
    )
    public void all(String car) {
        log.info("all cars : {}", car);
    }




    /** config based listener **/
    @RabbitListener(queues = {RabbitConfiguration.QUEUE_TOPIC_1})
    public void receiveTopicMessage1(final String message) {
        log.info("topic1 : ", message);
    }

    @RabbitListener(queues = {RabbitConfiguration.QUEUE_TOPIC_2})
    public void receiveTopicMessage2(final String message) {
        log.info("topic1 : ", message);
        if (1 == 1) {
            log.error("failed on received.");
            throw new IllegalStateException();

        }
    }
}
