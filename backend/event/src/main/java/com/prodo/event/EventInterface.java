package com.prodo.event;

import org.springframework.context.ApplicationEvent;

public class EventInterface extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public EventInterface(Object source) {
        super(source);
    }


    public class BalanceEvent extends  EventInterface {

        public BalanceEvent(Object source) {
            super(source);
        }
    }
}
