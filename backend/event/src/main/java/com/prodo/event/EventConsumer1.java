package com.prodo.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventConsumer1 {

    @EventListener
    public void listen(NameEvent event) {
        log.info("[EMITTER1111] event consume thread : {}", Thread.currentThread().getName());

    }

    @EventListener
    public void listen(PubEvent event) {
        log.info("[EMITTER1111] event consume thread : {}", Thread.currentThread().getName());
    }

}
