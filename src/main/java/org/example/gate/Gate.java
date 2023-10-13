package org.example.gate;


import org.example.flight.Flight;
import org.example.observer.EventManager;
import org.example.observer.EventType;

public class Gate implements Runnable {
    private static int number;
    private EventManager eventManager;
    private Flight flight;
    private EventType event;
    private boolean isOccupied;
    private final int gateNumber;
    private Thread gate;

    public Gate() {
        this.gateNumber = ++number;
        this.gate = new Thread(this);
        eventManager = EventManager.getInstance();
    }

    public Flight getFlight() {
        return flight;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public synchronized void notifyGate(EventType event, Flight f) {
        isOccupied = true;
        this.event  = event;
        this.flight = f;
        gate.start();
        gate = new Thread(this);
    }

    public void stop() {
        gate.interrupt();
    }

    @Override
    public void run() {
        if (event.equals(EventType.ARRIVING_GATE)) {
            System.out.println("Passengers of flight " + flight.getFlightNumber()+ " get of the plain!");
            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {

            }
            System.out.println("Passengers left the plain, gate number " + number + " opened!");
        } else {
            System.out.println("Passengers of flight " + flight.getFlightNumber()+ " get in the plain!");
            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {

            }
            System.out.println("Gate number " + number + " opened!");
            eventManager.notify(EventType.DEPARTURING_RUNWAY, flight);
        }

        flight = null;
        isOccupied = false;
        event = null;
    }

}
