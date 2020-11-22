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
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class User {

    private String id;
    private String username;
    private String imageURL;
    private String status;
    private HashMap<String, Integer> sports;
    private double latitude, longitude;


    public User(String id, String username, String imageURL, String status) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        this.sports = new HashMap<>();
    }

    public User() {

    }

    /**
     * this is really dumb but im keeping it as a reference
     */
//    public User(FirebaseUser user){
//        this.sports = new HashMap<>();
//        DatabaseReference reference = FirebaseDatabase
//                .getInstance()
//                .getReference("Users")
//                .child(user.getUid());
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User.this.username = dataSnapshot.child("username").getValue(String.class);
//                User.this.id = dataSnapshot.child("id").getValue(String.class);
//                User.this.imageURL = dataSnapshot.child("imageURL").getValue(String.class);
//                for(DataSnapshot d: dataSnapshot.child("sports").getChildren()){
//                    User.this.addSport(d.getKey(), d.getValue(Integer.class));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }

    public void addSport(String sport, int skill){
        if(sports.containsKey(sport))
            sports.remove(sport);
        sports.put(sport, skill);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HashMap<String, Integer> getSports(){
        return this.sports;
    }
}
