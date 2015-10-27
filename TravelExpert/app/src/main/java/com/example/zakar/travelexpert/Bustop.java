package com.example.zakar.travelexpert;

import java.util.Comparator;

public class Bustop implements Comparator<Bustop>, Comparable<Bustop>{

    private String stopName;
    private String stopCode;

     public Bustop(String stopName, String stopCode){
        this.stopCode = stopCode;
        this.stopName = stopName;
    }

    public String getStopName(){
        return stopName;
    }

    public String getStopCode(){
        return stopCode;
    }

    @Override
    public int compareTo(Bustop another) {
        return (this.stopName).compareTo(another.stopName);
    }

    @Override
    public int compare(Bustop lhs, Bustop rhs) {
        return 0;
    }
}
