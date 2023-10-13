package org.example.airport;


import org.example.airport_control.AirportControlTower;
import org.example.flight.ArrivalFlight;
import org.example.flight.DepartureFlight;
import org.example.flight.Flight;
import org.example.flight.FlightStatus;
import org.example.managers.GatesManager;
import org.example.managers.RunwaysManager;
import org.example.observer.EventManager;
import org.example.observer.EventType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.FileWriter;


public class Airport {
    private static volatile Airport instance;
    private static Lock lock = new ReentrantLock();
    private AirportControlTower controlTower;
    private EventManager eventManager;
    private final GatesManager gatesManager;
    private final RunwaysManager runwaysManager;
    private Map<String, Flight> flights;
    private Thread tower;
    private boolean stop;

    private Airport(int runways, int gates) {
        flights = new HashMap<>();
        runwaysManager = RunwaysManager.getInstance(runways);
        gatesManager = GatesManager.getInstance(gates);
        this.controlTower = AirportControlTower.getInstance();
        this.eventManager = EventManager.getInstance();
        subscribeManagers();
        tower = new Thread(controlTower);
        tower.start();
    }

    public static Airport getInstance(int runways, int gates) {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new Airport(runways, gates);
                }
            } finally {
                lock.unlock();
            }
        }

        return instance;
    }

    private void subscribeManagers() {
        eventManager.subscribe(EventType.ARRIVING, runwaysManager);
        eventManager.subscribe(EventType.DEPARTURING_RUNWAY, runwaysManager);
        eventManager.subscribe(EventType.DEPARTURING, gatesManager);
        eventManager.subscribe(EventType.ARRIVING_GATE, gatesManager);
    }

    public boolean isStop() {
        return stop;
    }
    public void stop() {
        controlTower.stop();
        runwaysManager.stop();
        gatesManager.stop();
        saveState();
    }

    private void saveState() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/ResultOfSimulation"))) {
            for (Map.Entry<String, Flight> entry : flights.entrySet()) {
                writer.write(String.valueOf(entry.getValue()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        stop = true;
    }

    public void addFlight(String flightNumber, String code, LocalDateTime scheduled, String type) {
        Flight flight = getFlight(flightNumber, code, scheduled, type);
        flights.put(flightNumber, flight);
        controlTower.addFlight(flight);
    }

    public void changeTimeOfFlight(String flightNumber, LocalDateTime estimatedTime) {
        Flight flight = isFlightExist(flightNumber);
        checkScheduling(estimatedTime);
        if (flight != null) {
            FlightStatus status = flight.getStatus();
            if (status.equals(FlightStatus.LANDED) || status.equals(FlightStatus.DEPARTED)) {
                throw new RuntimeException("Flight already " + status + "!");
            }
            if (!flight.getScheduled().equals(estimatedTime)) {
                controlTower.changeTimeOfFlight(flight, estimatedTime);
            }
        } else {
            throw new RuntimeException("Flight not found");
        }
    }

    public void cancelFlight(String flightNumber) {
        Flight f = isFlightExist(flightNumber);
        controlTower.cancelFlight(f);
    }

    private Flight getFlight(String flightNumber, String code, LocalDateTime scheduled, String type) {
        checkScheduling(scheduled);
        if (isFlightExist(flightNumber) != null) {
            throw new RuntimeException("Flight of number '" + flightNumber + "' already exist!");
        }
        Flight flight;
        type = type.toLowerCase();
        if (type.equals("arrival")) {
            flight = new ArrivalFlight(flightNumber, code, scheduled);
        } else if (type.equals("departure")) {
            flight = new DepartureFlight(flightNumber, code, scheduled);
        } else {
            throw new RuntimeException("Unsupported type of Flight '" + type + "'");
        }
        return flight;
    }

    private Flight isFlightExist(String flightNumber) {
        if (flights.containsKey(flightNumber)) {
            return flights.get(flightNumber);
        }

        return null;
    }

    private void checkScheduling(LocalDateTime scheduled) {
        if (scheduled.isBefore(LocalDateTime.now().plusMinutes(1))) {
            throw new IllegalStateException("Flights will be scheduled at least 1 minutes earlier!");
        }
    }

}
