package com.example.matchup.Adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.matchup.MessageActivity;
import com.example.matchup.Model.Chat;
import com.example.matchup.Model.Sport;
import com.example.matchup.Model.User;
import com.example.matchup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

//Displays other registered players
public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.ViewHolder> {

    private Context mContext;
    private List<DataSnapshot> mUsers;
    private boolean isChat;
    private double lat, lon;
    DecimalFormat dm = new DecimalFormat("#.#");

    String lastMessage;

    public PlayersAdapter(Context mContext, ArrayList<DataSnapshot> users, boolean isChat, double lat, double lon){
        this.mUsers = users;
        this.mContext = mContext;
        this.isChat = isChat;
        this.lat = lat;
        this.lon = lon;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.player_card, parent, false);
        return new PlayersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final DataSnapshot user = mUsers.get(holder.getAdapterPosition());

        final String id = (String)user.child("id").getValue();
        final String username = (String)user.child("username").getValue();
        final String description = (String)user.child("description").getValue();
        final String imageURL = (String)user.child("imageURL").getValue();
        final String status = (String)user.child("status").getValue();

        double userLat = 0.0;
        double userLon = 0.0;

        Object objLat = user.child("location").child("lat").getValue();
        Object objLon = user.child("location").child("lon").getValue();
        if(objLat!=null&&objLon!=null){
            userLat = (double)objLat;
            userLon = (double)objLon;
        }

        if(userLat == 0.0 || userLon == 0.0){
            holder.distance.setText("Unknown distance");
        } else {
            Location userLocation = new Location("a");
            userLocation.setLatitude(userLat);
            userLocation.setLongitude(userLon);
            Location currLocation = new Location("b");
            currLocation.setLatitude(lat);
            currLocation.setLongitude(lon);
            double dist = currLocation.distanceTo(userLocation);
            holder.distance.setText("Distance: " + dm.format(dist/1000d) + " km");
        }

        holder.username.setText(username);

        if(description==null)
            holder.description.setText("No description");
        else
            holder.description.setText(description);


        if(imageURL == null || imageURL.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(imageURL).into(holder.profile_image);
        }

        if(status == null || status.equals("offline")){
            holder.img_online.setVisibility(View.GONE);
            holder.img_offline.setVisibility(View.VISIBLE);
        } else {
            holder.img_online.setVisibility(View.VISIBLE);
            holder.img_offline.setVisibility(View.GONE);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", id);
                mContext.startActivity(intent);
            }
        });

        ArrayList<Sport> userSports = new ArrayList<>();
        for(DataSnapshot sportSnap: user.child("sports").getChildren()){
            userSports.add(new Sport(
                    (String)sportSnap.getKey(),
                    ((Number)sportSnap.getValue()).intValue()
            ));
        }
        holder.sportList.setAdapter(new SportListAdapter(userSports));
        holder.sportList.setLayoutManager(new LinearLayoutManager(mContext));

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView username, description, distance;
        ImageView profile_image;
        ImageView img_online;
        ImageView img_offline;
        RecyclerView sportList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            distance = itemView.findViewById(R.id.distance);
            username = itemView.findViewById(R.id.username);
            description = itemView.findViewById(R.id.player_description);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_online = itemView.findViewById(R.id.img_online);
            img_offline = itemView.findViewById(R.id.img_offline);
            sportList = itemView.findViewById(R.id.sportlist);
        }
    }

    class SportListAdapter extends RecyclerView.Adapter<SportListAdapter.ViewHolder> {

        ArrayList<Sport> mSports;
        SportListAdapter(ArrayList<Sport> sports){
            this.mSports = sports;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.player_card_sport_item, parent, false);
            return new SportListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Sport sport = mSports.get(position);
            holder.name.setText(sport.sportName);
            holder.proficiency.setText(sport.getProficiencyString());
            int textColor;
            switch(sport.proficiency){
                case 1: textColor = R.color.textColorGreen; break;
                case 2: textColor = R.color.textColorOrange; break;
                case 3: textColor = R.color.textColorRed; break;
                default: textColor = R.color.colorPrimaryDark;
            }
            holder.proficiency.setTextColor(ContextCompat.getColor(mContext, textColor));
        }

        @Override
        public int getItemCount() {
            return mSports.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, proficiency;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.sportName);
                proficiency = itemView.findViewById(R.id.proficiency);
            }
        }
    }
}
