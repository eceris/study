package com.prodo.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublisher {
    private final ApplicationEventPublisher publisher;

    public void get(String name) {
        log.info("event publish thread : {}", Thread.currentThread().getName());
        log.info("event publish time : {}", System.currentTimeMillis());
        publisher.publishEvent(new NameEvent(NameEvent.Type.GET, name));
    }

    public void post(String name) {
        log.info("event publish thread : {}", Thread.currentThread().getName());
        log.info("event publish time : {}", System.currentTimeMillis());
        publisher.publishEvent(new PubEvent(PubEvent.Type.POST, name));
    }



}

