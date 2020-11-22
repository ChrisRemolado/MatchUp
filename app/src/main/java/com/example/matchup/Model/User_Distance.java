package com.example.matchup.Model;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class User_Distance {

    private String id;
    private double distance;

    public User_Distance(String id, double distance){
        this.id = id;
        this.distance = distance;
    }

    public User_Distance(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDistance() { return distance; }

    public void setDistance(double distance) { this.distance = distance; }

}
