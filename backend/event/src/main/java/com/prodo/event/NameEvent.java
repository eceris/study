package com.prodo.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NameEvent extends ApplicationEvent {
    private String name;
    private Type type;

    public NameEvent(Type type, String name) {
        super(name);
        this.type = type;
        this.name = name;
    }

    public enum Type {
        GET, POST;
    }
}
