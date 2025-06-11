package com.example.rt.Model;

import java.io.Serializable;

public class Flight implements Serializable {
    private String trainLogo;
    private String trainName;
    private String arriveTime;
    private String classSeat;
    private String date;
    private String from;
    private String fromShort;
    private Integer numberSeat;
    private Double price;
    private String passenger;
    private String seats;
    private String reservedSeats;
    private String time;
    private String to;
    private String toShort;

    public Flight() {
        // Default constructor required for calls to DataSnapshot.getValue(Flight.class)
    }

//    // You had a constructor public Flight(String trainLogo) {}.
//    // If you intend to use a full constructor, it should include all fields.
//    // For Firebase, typically only the no-arg constructor and getters/setters are strictly needed.
//    // However, if you're creating Flight objects in your app, a full constructor is useful.
    public Flight(String  trainLogo, String trainName, String arriveTime, String classSeat, String date,
                  String from, String fromShort, Integer numberSeat, Double price, String passenger,
                  String seats, String reservedSeats, String time, String to, String toShort) {
        this.trainLogo = trainLogo;
        this.trainName = trainName;
        this.arriveTime = arriveTime;
        this.classSeat = classSeat;
        this.date = date;
        this.from = from;
        this.fromShort = fromShort;
        this.numberSeat = numberSeat;
        this.price = price;
        this.passenger = passenger;
        this.seats = seats;
        this.reservedSeats = reservedSeats;
        this.time = time;
        this.to = to;
        this.toShort = toShort;
    }


    @Override
    public String toString() {
        return from;
    }

    public String getTrainLogo() {
        return trainLogo;
    }

    public void setTrainLogo(String trainLogo) {
        this.trainLogo = trainLogo;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getClassSeat() {
        return classSeat;
    }

    public void setClassSeat(String classSeat) {
        this.classSeat = classSeat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromShort() {
        return fromShort;
    }

    public void setFromShort(String fromShort) {
        this.fromShort = fromShort;
    }

    // Corrected getter and setter for numberSeat
    public Integer getNumberSeat() {
        return numberSeat != null ? numberSeat : 0;
    }

    public void setNumberSeat(  int numberSeat) {
        this.numberSeat = numberSeat;
    }

    // Corrected getter and setter for price
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    // Corrected getter and setter for seats
    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    // Corrected getter and setter for reservedSeats
    public String getReservedSeats() {
        return reservedSeats;
    }

    public void setReservedSeats(String reservedSeats) {
        this.reservedSeats = reservedSeats;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToShort() {
        return toShort;
    }

    public void setToShort(String toShort) {
        this.toShort = toShort;
    }
}