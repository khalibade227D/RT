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
    private String numberSeat;
    private String price;
    private String passenger;
    private String seats;
    private String reservedSeats;
    private String time;
    private String to;
    private String toShort;

    public Flight(String trainLogo) {

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

    public String getNumberSeat() {
        return numberSeat;
    }

    public void setNumberSeat(String numberSeat) {
        this.numberSeat = numberSeat;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

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
