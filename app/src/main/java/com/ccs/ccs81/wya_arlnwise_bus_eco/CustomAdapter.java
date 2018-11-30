package com.ccs.ccs81.wya_arlnwise_bus_eco;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Banke Bihari on 05/12/2017.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<Movie> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewAirlineCode,textViewBranch,textViewBusiness,textViewBusinessAmount,textViewEco,textViewEcoAmount,textViewFirst,textViewFirstAmount,textViewPremium,textViewPremiumAmount,textViewAirlineName;
        public ImageView airlineLogo;

        public MyViewHolder(View view) {
            super(view);
            airlineLogo = (ImageView) view.findViewById(R.id.airlinelogo);
            textViewAirlineCode  = (TextView) view.findViewById(R.id.tv_airline);
            textViewBranch  = (TextView) view.findViewById(R.id.tv_branch);
            textViewBusiness = (TextView) view.findViewById(R.id.tv_business);
            textViewBusinessAmount = (TextView) view.findViewById(R.id.tv_businessAmount);
            textViewEco = (TextView) view.findViewById(R.id.tv_eco);
            textViewEcoAmount = (TextView) view.findViewById(R.id.tv_ecoAmount);
            textViewFirst = (TextView) view.findViewById(R.id.tv_first);
            textViewFirstAmount = (TextView) view.findViewById(R.id.tv_firstAmount);
            textViewPremium = (TextView) view.findViewById(R.id.tv_premium);
            textViewPremiumAmount = (TextView) view.findViewById(R.id.tv_premiumAmount);
            textViewAirlineName = (TextView) view.findViewById(R.id.tv_airlineName);

        }
    }


    public CustomAdapter(List<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.color.LightGrey);
        } else {
            holder.itemView.setBackgroundResource(R.color.White);
        }

        Movie movie = moviesList.get(position);
        holder.airlineLogo.setImageResource(movie.getImage());
        holder.textViewAirlineCode.setText(movie.getAirlineCode());
        holder.textViewBranch.setText(movie.getBranch());
        holder.textViewBusiness.setText(movie.getBusiness());
        holder.textViewBusinessAmount.setText(movie.getBusinessAmount());
        holder.textViewEco.setText(movie.getEco());
        holder.textViewEcoAmount.setText(movie.getEcoAmount());
        holder.textViewFirst.setText(movie.getFirst());
        holder.textViewFirstAmount.setText(movie.getFirstAmount());
        holder.textViewPremium.setText(movie.getPremium());
        holder.textViewPremiumAmount.setText(movie.getPremiumAmount());
        holder.textViewAirlineName.setText(movie.getAirlineName());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}