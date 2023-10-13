package org.example.managers;


import org.example.flight.Flight;
import org.example.gate.Gate;
import org.example.observer.EventListener;
import org.example.observer.EventType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Iterator;

public class GatesManager implements EventListener {
    private static volatile GatesManager instance;
    private static Lock lock = new ReentrantLock();
    private List<Gate> gates;

    private GatesManager(int gates) {
        this.gates = new CopyOnWriteArrayList<>();
        fillGatesList(gates);
    }

    private void fillGatesList(int gates) {
        for (int i = 0; i < gates; ++i) {
            this.gates.add(new Gate());
        }
    }

    public static GatesManager getInstance(int gates) {
        if (instance == null) {
            try {
                lock.lock();
                if (instance == null) {
                    instance = new GatesManager(gates);
                }
            } finally {
                lock.unlock();
            }
        }

        return instance;
    }

    public void stop() {
        Iterator<Gate> iter = gates.iterator();
        while (iter.hasNext()) {
            iter.next().stop();
        }
    }

    @Override
    public void notifyListener(EventType event, Flight flight) {
        Gate g;
        boolean isNotified = false;
        Iterator<Gate> iter = gates.iterator();
        while(!isNotified) {
            if (iter.hasNext()) {
                g = iter.next();
                if (!g.isOccupied()) {
                    g.notifyGate(event, flight);
                    isNotified = true;
                }
            } else {
                iter = gates.iterator();
            }
        }
    }

}
