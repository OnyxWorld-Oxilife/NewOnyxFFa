package fr.vivicoubar.onyxffa.utils;

import fr.vivicoubar.onyxffa.OnyxFFaMain;

import java.util.List;

public class Rank {
    String name;
    String color;
    double upperBound;
    double lowerBound;
    double rankNumber;
    OnyxFFaMain main;
    List<String> commandOnLeaveRank;
    List<String> commandOnGoToRank;

     public Rank(OnyxFFaMain onyxFFaMain, String name, String color, double upperBound, double lowerBound,double rankNumber, List<String> commandOnGoToRank, List<String> commandOnLeaveRank){
        this.color = color;
        this.main = onyxFFaMain;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.name = name;
        this.rankNumber = rankNumber;
        this.commandOnGoToRank = commandOnGoToRank;
        this.commandOnLeaveRank = commandOnLeaveRank;
    }

    public String getColor() {
        return this.color;
    }
    public String getName() {
        return this.name;
    }
    public double getRankNumber(){
        return this.rankNumber;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public List<String> getCommandOnGoToRank() {
        return commandOnGoToRank;
    }

    public List<String> getCommandOnLeaveRank() {
        return commandOnLeaveRank;
    }
}

