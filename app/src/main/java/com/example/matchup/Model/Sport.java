package com.example.matchup.Model;

public class Sport {

    public static final String[] SPORT_LIST = {
            "Soccer",
            "Baseball",
            "Football",
            "Basketball",
            "Hockey",
            "Rock Climbing",
            "Lifting",
            "Swimming",
            "Track",
    };

    public String sportName;
    public int proficiency;

    public Sport(String sportName, int proficiency){
        this.sportName = sportName;
        this.proficiency = proficiency;
    }

    public String getProficiencyString(){
        switch(proficiency){
            case 1: return "Beginner";
            case 2: return "Advanced";
            case 3: return "Pro";
            default: return "";
        }
    }
}
