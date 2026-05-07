package com.flight;

public class Flight {
    private String flightNumber;
    private String destination;
    private String departureTime;
    private double price;

    public Flight(String flightNumber, String destination, String departureTime, double price) {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.departureTime = departureTime;
        this.price = price;
    }

    public String getFlightNumber() { return flightNumber; }
    public String getDestination() { return destination; }
    public String getDepartureTime() { return departureTime; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return String.format("%-10s %-15s %-10s ₹%.2f",
                flightNumber, destination, departureTime, price);
    }
}