package org.example.flight;


import java.time.LocalDateTime;

public class DepartureFlight extends Flight{

    public DepartureFlight(String flightNumber, String code, LocalDateTime scheduled) {
        super(flightNumber, code, scheduled);
    }

}
