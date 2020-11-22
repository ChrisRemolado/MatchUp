package com.example.matchup.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.matchup.Adapter.PlayersAdapter;
import com.example.matchup.Model.User;
import com.example.matchup.Model.User_Distance;
import com.example.matchup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

//Displays other registered players
//TODO:
// (a) Display registers players from closest distance to farthest distance
// (b) onShortClick display user profile --> open another activity
public class PlayersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private PlayersAdapter playersAdapter;
    private ArrayList<DataSnapshot> mUsers;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_players, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);

        mUsers = new ArrayList<>();

        readUsers();

        return view;
    }

    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(getActivity()==null) return;
                mUsers.clear();
                double lat = 0.0, lon = 0.0;
                ArrayList<String> user_sports = new ArrayList<>();
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    if(snap.getKey()!=firebaseUser.getUid())
                        mUsers.add(snap);
                    else {
                        Object objLat = snap.child("location").child("lat").getValue();
                        Object objLon = snap.child("location").child("lon").getValue();
                        if(objLat != null && objLon != null){
                            lat = (double) objLat;
                            lon = (double) objLon;
                        }
                        // make arraylist of sports of the user
                        for(DataSnapshot sport_name: snap.child("sports").getChildren()){
                            user_sports.add(sport_name.getKey());
                        }
                    }
                }
                // Let's create an ArrayList of User_Distance objects
                ArrayList<User_Distance> user_distances = new ArrayList<User_Distance>();
                boolean key = false;
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    if(snap.getKey() != firebaseUser.getUid()){
                        // check if common sports are available with the user, and only add to list if it is
                        key = false;
                        for(DataSnapshot sport_name: snap.child("sports").getChildren()){
                            if(user_sports.contains(sport_name.getKey())){
                                key = true;
                            }
                        }
                        if(key == true){
                            if (snap.child("location").child("lat").getValue() != null && snap.child("location").child("lon").getValue() != null) {
                                User_Distance temp = new User_Distance();
                                temp.setId(snap.getKey());
                                temp.setDistance(distance(lat, lon, (double) snap.child("location").child("lat").getValue(), (double) snap.child("location").child("lon").getValue()));
                                user_distances.add(temp);
                            }
                        }
                    }
                }
                // Need to sort user_distances arraylist based on distance value
                int n = user_distances.size();
                for (int i = 0; i < n-1; i++) {
                    int min_idx = i;
                    for (int j = i+1; j < n; j++) {
                        if (user_distances.get(j).getDistance() < user_distances.get(min_idx).getDistance()) {
                            min_idx = j;
                        }
                    }
                    /*
                    User_Distance temp = user_distances.get(min_idx);
                    user_distances.set(min_idx, user_distances.get(i));
                    user_distances.set(i, temp);
                    */
                    Collections.swap(user_distances, min_idx, i);
                }
                // Then based on sorting, remake the mUsers array and apply to adapter to show
                n = user_distances.size();
                int n2 = mUsers.size();
                for (int i = 0; i < n; i++) { // this will traverse through user distances
                    for (int j = 0; j < n2; j++) { // this will traverse through mUsers
                        if (user_distances.get(i).getId() == mUsers.get(j).getKey()){
                            Collections.swap(mUsers, i, j);
                            break;
                        }
                    }
                }
                playersAdapter = new PlayersAdapter(getContext(), mUsers, false, lat, lon);
                recyclerView.setAdapter(playersAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    public void onRefresh() {
        readUsers();
    }

    public double distance(double lat1, double lon1, double lat2, double lon2){
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        return dist;
    }
}
