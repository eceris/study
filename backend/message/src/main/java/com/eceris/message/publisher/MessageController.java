package com.eceris.message.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

import static com.eceris.message.config.RabbitConfiguration.FANOUT_EXCHANGE_NAME;
import static com.eceris.message.config.RabbitConfiguration.TOPIC_EXCHANGE_NAME;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MessageController {


    private final RabbitTemplate rabbitTemplate;

    private final AtomicInteger counter = new AtomicInteger(0);

    @GetMapping("/api/topic")
    public String pubTopic() {
        log.info("send message to topic exchange");
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_NAME, "foo.bar.a", "Hello Message!" + counter.getAndIncrement());
        return "OK";
    }

    @GetMapping("/api/fanout")
    public String pubFanout() {
        log.info("send message to fanout exchange");
        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE_NAME, "", "Hello Message!" + counter.getAndIncrement());
        return "OK";
    }
}
