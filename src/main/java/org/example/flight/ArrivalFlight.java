package org.example.flight;


import java.time.LocalDateTime;

public class ArrivalFlight extends Flight{
    public ArrivalFlight(String flightNumber, String code, LocalDateTime scheduled) {
        super(flightNumber, code, scheduled);
    }

}
