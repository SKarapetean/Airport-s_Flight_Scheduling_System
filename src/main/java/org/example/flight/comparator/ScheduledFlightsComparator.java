package org.example.flight.comparator;

import org.example.flight.Flight;

import java.util.Comparator;

public class ScheduledFlightsComparator implements Comparator<Flight> {
    @Override
    public int compare(Flight f1, Flight t2) {
        return f1.getScheduled().compareTo(t2.getScheduled());
    }

}
