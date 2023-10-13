package org.example.observer;

import org.example.flight.Flight;

public interface EventListener {

    void notifyListener(EventType event, Flight flight);

}
