package com.eceris.message.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;

import java.nio.charset.StandardCharsets;

@Slf4j
public class RabbitMqExceptionHandler extends RejectAndDontRequeueRecoverer {
    @Override
    public void recover(Message message, Throwable cause) {

        final byte[] body = message.getBody();
        final String msg = new String(body, StandardCharsets.UTF_8);
        log.error("===================");
        log.debug(msg);
        log.warn("Retries exhausted for listener " + message, cause);
        log.error("===================");
    }

}