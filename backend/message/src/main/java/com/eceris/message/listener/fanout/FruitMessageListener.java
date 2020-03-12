package com.eceris.message.listener.fanout;

import com.eceris.message.config.RabbitConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FruitMessageListener {

    /** annotation based listener **/
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "apple_queue", durable = "true"),
            exchange = @Exchange(value = RabbitConfiguration.FANOUT_EXCHANGE_NAME, ignoreDeclarationExceptions = "true"),
            key = "")
    )
    public void apple(String fruit) {
        log.info("apple : {}", fruit);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "banana_queue", durable = "true"),
            exchange = @Exchange(value = RabbitConfiguration.FANOUT_EXCHANGE_NAME, ignoreDeclarationExceptions = "true"),
            key = "")
    )
    public void banana(String fruit) {
        log.info("banana : {}", fruit);
    }




    /** config based listener **/
    @RabbitListener(queues = {RabbitConfiguration.QUEUE_FANOUT_1})
    public void receiveFanoutMessage(final String message) {
        log.info("fanout1 : ", message);
    }

    @RabbitListener(queues = {RabbitConfiguration.QUEUE_FANOUT_2})
    public void receiveFanoutMessage2(final String message) {
        log.info("fanout2 : ", message);
    }


}
