package com.prodo.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PubEvent extends ApplicationEvent {
    private String name;
    private Type type;

    public PubEvent(Type type, String name) {
        super(name);
        this.type = type;
        this.name = name + "pub!!";
    }

    public enum Type {
        GET, POST;
    }
}
