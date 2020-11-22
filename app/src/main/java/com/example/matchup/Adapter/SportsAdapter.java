package com.example.matchup.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matchup.Model.Sport;
import com.example.matchup.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.ViewHolder> {

    private List<Sport> chosenSports;
    private Context mContext;
    private OnSportChange callback;

    public interface OnSportChange {
        void onDeleteSport(String sportName);
        void onProficiencyChange(String sportName, int proficiency);
    }

    public SportsAdapter(Context mContext, OnSportChange callback){
        this.mContext = mContext;
        this.chosenSports = new ArrayList<>();
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sport_item, parent, false);
        return new SportsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                if(value < 1 || value > 3) return;
                chosenSports.get(holder.getAdapterPosition()).proficiency = value;
                holder.proficiency.setText(chosenSports.get(holder.getAdapterPosition()).getProficiencyString());
                callback.onProficiencyChange(chosenSports.get(holder.getAdapterPosition()).sportName, value);
            }
            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) { }
        });

        holder.seekBar.setProgress(chosenSports.get(holder.getAdapterPosition()).proficiency);

        holder.sportName.setText(chosenSports.get(holder.getAdapterPosition()).sportName);
        holder.proficiency.setText(chosenSports.get(holder.getAdapterPosition()).getProficiencyString());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            callback.onDeleteSport(chosenSports.get(holder.getAdapterPosition()).sportName);

            chosenSports.remove(holder.getAdapterPosition());
            notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.chosenSports.size();
    }

    public void setSports(Collection<Sport> sports){
        chosenSports.clear();
        for(Sport sp: sports)
            if(sp != null)
                chosenSports.add(sp);
        notifyDataSetChanged();
    }

    public void addSport(Sport sport){
        if(sport == null) return;
        chosenSports.add(sport);
        notifyDataSetChanged();
    }

    public List<Sport> getSports(){
        return this.chosenSports;
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        DiscreteSeekBar seekBar;
        TextView proficiency, sportName;
        Button deleteButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            sportName = itemView.findViewById(R.id.sport);
            seekBar = itemView.findViewById(R.id.seekbar);
            proficiency = itemView.findViewById(R.id.proficiency);
            deleteButton = itemView.findViewById(R.id.btndelete);
        }
    }


}
