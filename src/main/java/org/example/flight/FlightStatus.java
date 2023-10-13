package org.example.flight;

public enum FlightStatus {
    ON_TIME("On time"),
    DELAY("Delay"),
    EARLIER("Earlier"),
    LANDED("Landed"),
    DEPARTED("Departed"),
    CANCELLED("Cancelled");

    private final String status;

    FlightStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return status;
    }

}
