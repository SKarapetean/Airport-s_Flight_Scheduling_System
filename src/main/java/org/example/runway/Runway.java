package org.example.runway;


import org.example.flight.Flight;
import org.example.flight.FlightStatus;
import org.example.observer.EventManager;
import org.example.observer.EventType;

public class Runway implements Runnable {

    private static int number;
    private EventManager eventManager;
    private Flight flight;
    private boolean isOccupied;
    private EventType event;
    private Thread runway;
    private int runwayNumber;

    public Runway() {
        this.runwayNumber = ++number;
        this.runway = new Thread(this);
        eventManager = EventManager.getInstance();
    }
    public Flight getFlight() {
        return flight;
    }

    public boolean isOccupied() {
        return isOccupied;
    }


    public void notifyRunway(EventType event, Flight flight) {
        isOccupied = true;
        this.event = event;
        this.flight = flight;
        runway.start();
        runway = new Thread(this);
    }

    public void stop() {
        runway.interrupt();
    }

    @Override
    public void run() {
        if (event.equals(EventType.ARRIVING)) {
            System.out.println("Flight number " + flight.getFlightNumber() + " starts landing!");
            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {

            }
            System.out.println("Flight number " + flight.getFlightNumber() + " arrived");
            flight.setStatus(FlightStatus.LANDED);
            eventManager.notify(EventType.ARRIVING_GATE, flight);
        } else {
            System.out.println("Flight number " + flight.getFlightNumber() + " starts taking of!");
            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {

            }
            System.out.println("Flight number " + flight.getFlightNumber() + " departed!");
            flight.setStatus(FlightStatus.DEPARTED);
        }

        flight = null;
        isOccupied = false;
        event = null;
    }

}
