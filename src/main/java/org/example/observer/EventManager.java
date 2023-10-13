package org.example.observer;


import org.example.flight.Flight;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EventManager {
    private static volatile EventManager instance;
    private static Lock lock = new ReentrantLock();
    private Map<EventType, EventListener> listeners;

    private EventManager() {
        listeners = new HashMap<>();
    }

    public static EventManager getInstance() {
        if (instance == null) {
            try {
                lock.lock();
                if (instance == null) {
                    instance = new EventManager();
                }
            } finally {
                lock.unlock();
            }
        }

        return instance;
    }

    public void subscribe(EventType event, EventListener eventListener) {
        listeners.put(event, eventListener);
    }

    public void notify(EventType event, Flight flight) {
        listeners.get(event).notifyListener(event, flight);
    }

}