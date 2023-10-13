package org.example.flight;


import java.time.LocalDateTime;

public abstract class Flight {
    private String flightNumber;
    private String code;
    private LocalDateTime scheduled;
    private LocalDateTime estimatedTime;
    private FlightStatus status;

    public Flight(String flightNumber, String code, LocalDateTime scheduled) {
        this.flightNumber = flightNumber;
        this.code = code;
        this.scheduled = scheduled;
        this.status = FlightStatus.ON_TIME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Flight flight = (Flight) o;
        return flightNumber.equals(flight.flightNumber) && code.equals(flight.code);
    }

    @Override
    public int hashCode() {
        return flightNumber.hashCode();
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber='" + flightNumber + '\'' +
                ", code='" + code + '\'' +
                ", scheduled=" + scheduled +
                ", estimatedTime=" + estimatedTime +
                ", status=" + status +
                '}';
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public LocalDateTime getScheduled() {
        return scheduled;
    }

    public void setScheduled(LocalDateTime scheduled) {
        this.scheduled = scheduled;
    }

    public LocalDateTime getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(LocalDateTime estimatedTime) {
        if (scheduled.isBefore(estimatedTime)) {
            this.estimatedTime = estimatedTime;
            status = FlightStatus.EARLIER;
        } else if (scheduled.isAfter(estimatedTime)) {
            this.estimatedTime = estimatedTime;
            status = FlightStatus.DELAY;
        }
    }
    public String getFlightNumber() {
        return flightNumber;
    }

}
