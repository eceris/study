package com.eceris.message.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.eceris.message.config.RabbitConfiguration.FANOUT_EXCHANGE_NAME;
import static com.eceris.message.config.RabbitConfiguration.TOPIC_EXCHANGE_NAME;

@Component
@RequiredArgsConstructor
public class MessageScheduler {

    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedRate = 30000, initialDelay = 3000)
    private void car() {
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_NAME, "dodge.cars.usa", "yoyo");
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_NAME, "ford.cars.usa", "fordman");
    }

//    @Scheduled(fixedRate = 30000, initialDelay = 3000)
//    private void fruit() {
//        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE_NAME, "", "yoyo");
//        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE_NAME, "", "fordman");
//    }
}
