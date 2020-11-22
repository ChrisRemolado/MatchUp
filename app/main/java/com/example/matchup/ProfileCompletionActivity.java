package com.example.matchup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matchup.Adapter.SportsAdapter;
import com.example.matchup.Model.Sport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.LinkedList;

public class ProfileCompletionActivity extends AppCompatActivity implements SportsAdapter.OnSportChange {

    private LinkedList<String> spinnerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spinnerList = new LinkedList<>(Arrays.asList(Sport.SPORT_LIST));
        spinnerList.add(0, "Choose sport");
        setContentView(R.layout.activity_profile_completion);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Finalize");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnComplete = findViewById(R.id.btn_complete);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);


        final SportsAdapter sportsAdapter = new SportsAdapter(this, this);
        recyclerView.setAdapter(sportsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final Spinner spinner = findViewById(R.id.spinner);
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0) return;
                Sport sp = new Sport(spinnerList.get(position), 1);
                sportsAdapter.addSport(sp);
                spinnerList.remove(position);
                spinnerAdapter.notifyDataSetChanged();
                spinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sportsAdapter.getSports().size() < 1){
                    Toast.makeText(ProfileCompletionActivity.this, "Add some sports to your profile", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                DatabaseReference sportsList = reference.child("sports");

                for(Sport sport: sportsAdapter.getSports())
                    sportsList.child(sport.sportName).setValue(sport.proficiency);

                Intent intent = new Intent(ProfileCompletionActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

    public void onDeleteSport(String sportName){
        spinnerList.add(sportName);
    }

    public void onProficiencyChange(String sportName, int proficiency){ }
}
