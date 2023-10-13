package org.example.airport_control;


import org.example.flight.ArrivalFlight;
import org.example.flight.Flight;
import org.example.flight.FlightStatus;
import org.example.flight.comparator.EstimatedFlightsComparator;
import org.example.flight.comparator.ScheduledFlightsComparator;
import org.example.observer.EventManager;
import org.example.observer.EventType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AirportControlTower implements Runnable{
    private static volatile AirportControlTower instance;
    private static final Lock lock = new ReentrantLock();
    private final EventManager eventManager;
    private final PriorityQueue<Flight> scheduledFlights;
    private final PriorityQueue<Flight> estimatedFlights;
    private Thread airportControlTower;
    private AirportControlTower() {
        scheduledFlights = new PriorityQueue<>(new ScheduledFlightsComparator());
        estimatedFlights = new PriorityQueue<>(new EstimatedFlightsComparator());
        this.eventManager = EventManager.getInstance();
        airportControlTower = new Thread(this);
    }

    public static AirportControlTower getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new AirportControlTower();
                }
            } finally {
                lock.unlock();
            }
        }

        return instance;
    }

    public void stop() {
        airportControlTower.interrupt();
    }

    public void addFlight(Flight flight) {
        synchronized (scheduledFlights) {
            scheduledFlights.offer(flight);
        }
    }

    public void changeTimeOfFlight(Flight flight, LocalDateTime estimatedTime) {
        Iterator<Flight> iterator;
        if (flight.getEstimatedTime() == null) {
            iterator = scheduledFlights.iterator();
            synchronized (scheduledFlights) {
                removeFlightFromQueue(flight, iterator);
            }
        } else {
            iterator = estimatedFlights.iterator();
            synchronized (scheduledFlights) {
                removeFlightFromQueue(flight, iterator);
            }
        }
        flight.setEstimatedTime(estimatedTime);
        estimatedFlights.offer(flight);
    }

    public void cancelFlight(Flight flight) {
        Iterator<Flight> iterator;
        if (flight.getEstimatedTime() == null) {
            iterator = scheduledFlights.iterator();
            synchronized (scheduledFlights) {
                removeFlightFromQueue(flight, iterator);
            }
        } else {
            iterator = estimatedFlights.iterator();
            synchronized (estimatedFlights) {
                removeFlightFromQueue(flight, iterator);
            }
        }
        flight.setStatus(FlightStatus.CANCELLED);
    }

    private void removeFlightFromQueue(Flight flight, Iterator<Flight> iterator) {
        while(iterator.hasNext()) {
            Flight tmp = iterator.next();
            if (tmp.equals(flight)) {
                iterator.remove();
            }
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (!estimatedFlights.isEmpty()) {
                Flight f1 = scheduledFlights.peek();
                Flight f2 = estimatedFlights.peek();
                if (f1 != null && f1.getScheduled().isBefore(f2.getEstimatedTime())) {
                    doEventScheduled(f1);
                } else {
                    doEventEstimated(f2);
                }
            } else {
                Flight f = scheduledFlights.peek();
                if (f != null) {
                    doEventScheduled(f);
                }
            }
        }
    }

    private void doEventScheduled(Flight f) {
        if (isTimeOfStartingEvent(f.getScheduled())) {
            if (f instanceof ArrivalFlight) {
                eventManager.notify(EventType.ARRIVING, f);
            } else {
                eventManager.notify(EventType.DEPARTURING, f);
            }
            synchronized (scheduledFlights) {
                scheduledFlights.poll();
            }
        }
    }

    private void doEventEstimated(Flight f) {
        if (isTimeOfStartingEvent(f.getEstimatedTime())) {
            if (f instanceof ArrivalFlight) {
                eventManager.notify(EventType.ARRIVING, f);
            } else {
                eventManager.notify(EventType.DEPARTURING, f);
            }
            synchronized (estimatedFlights) {
                estimatedFlights.poll();
            }
        }
    }

    private boolean isTimeOfStartingEvent(LocalDateTime timeOfFlight) {
        LocalDateTime currentTime = LocalDateTime.now();
        long minutesDifference = ChronoUnit.MINUTES.between(currentTime, timeOfFlight);
        return minutesDifference <= 2;
    }

}
