package org.example.flight.comparator;

import org.example.flight.Flight;

import java.util.Comparator;

public class EstimatedFlightsComparator implements Comparator<Flight> {
    @Override
    public int compare(Flight f1, Flight f2) {
        return f1.getEstimatedTime().compareTo(f2.getEstimatedTime());
    }

}
