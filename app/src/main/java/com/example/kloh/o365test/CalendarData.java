package com.example.kloh.o365test;

/**
 * Created by nagank on 12/10/2014.
 */
public class CalendarData {
    int numberofevents=0;
    int numberofeventsindiffbuilding=0;
    int numberofformalevents=0;
    boolean isRainy=true;
    int weather=50;

    public CalendarData(int numberofevents, int numberofeventsindiffbuilding, int numberofformalevents, boolean isRainy, int weather)
    {
        this.isRainy=isRainy;
        this.numberofevents=numberofevents;
        this.numberofeventsindiffbuilding=numberofeventsindiffbuilding;
        this.numberofformalevents=numberofformalevents;
        this.weather=weather;
    }
}
