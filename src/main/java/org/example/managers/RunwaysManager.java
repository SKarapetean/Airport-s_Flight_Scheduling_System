package org.example.managers;


import org.example.flight.Flight;
import org.example.observer.EventListener;
import org.example.observer.EventType;
import org.example.runway.Runway;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class RunwaysManager implements EventListener {
    private static volatile RunwaysManager instance;
    private static Lock lock = new ReentrantLock();
    private List<Runway> runways;

    private RunwaysManager(int runways) {
        this.runways = new CopyOnWriteArrayList<>();
        fillRunwaysList(runways);
    }

    private void fillRunwaysList(int runways) {
        for (int i = 0; i < runways; ++i) {
            this.runways.add(new Runway());
        }
    }

    public static RunwaysManager getInstance(int runways) {
        if (instance == null) {
            try {
                lock.lock();
                if (instance == null) {
                    instance = new RunwaysManager(runways);
                }
            } finally {
                lock.unlock();
            }
        }

        return instance;
    }

    public void stop() {
        Iterator<Runway> iter = runways.iterator();
        while (iter.hasNext()) {
            iter.next().stop();
        }
    }

    @Override
    public void notifyListener(EventType event, Flight flight) {
        Runway r;
        boolean isNotified = false;
        Iterator<Runway> iter = runways.iterator();
        while(!isNotified) {
            if (iter.hasNext()) {
                r = iter.next();
                if (!r.isOccupied()) {
                    r.notifyRunway(event, flight);
                    isNotified = true;
                }
            } else {
                iter = runways.iterator();
            }
        }
    }

}
