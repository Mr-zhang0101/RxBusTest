package com.zhang.rxbustest.event;


public class Event_Sticky {
    public String event;

    public Event_Sticky(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "EventSticky{" +
                "event='" + event + '\'' +
                '}';
    }
}
