package com.example.greentrailapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.greentrailapp.InfoActivity;
import com.example.greentrailapp.Models.Marker;
import com.example.greentrailapp.R;

import java.util.ArrayList;
import java.util.List;

public class Marker_RecyclerViewAdapter extends RecyclerView.Adapter<Marker_RecyclerViewAdapter.myViewHolder> {

    Context context;
    private List<Marker> markerList;
    public CardView cardView;

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView mName;

        myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView)itemView.findViewById(R.id.ivMarker);
            mName = (TextView)itemView.findViewById(R.id.mNameTV);
        }
    }

    public Marker_RecyclerViewAdapter(Context context, ArrayList<Marker> markerList){
        this.context = context;
        this.markerList = markerList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.marker_recycler_view_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Marker currentMarker = markerList.get(position);
        holder.mName.setText(currentMarker.getmName());
        Glide.with(holder.img.getContext())
                .load(currentMarker.getImg_url())
                .into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                Intent intent = new Intent(context, InfoActivity.class);
                intent.putExtra("Marker", currentMarker);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {return markerList.size();}

    public void setFilteredList(List<Marker> filteredList){
        this.markerList = filteredList;
        notifyDataSetChanged();
    }
}
