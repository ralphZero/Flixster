package com.ralph.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ralph.flixster.R;
import com.ralph.flixster.models.Movies;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView poster;
        public TextView title;
        public TextView overview;

        public ViewHolder(View itemView) {
            super(itemView);

            poster = (ImageView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            overview = (TextView) itemView.findViewById(R.id.tvOverview);

        }
    }

    private List<Movies> moviesList;
    private Context context;

    public MovieAdapter(List<Movies> moviesList,Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View mView = inflater.inflate(R.layout.custom_row,parent,false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movies movies = moviesList.get(position);
        TextView tvTitle = holder.title;
        tvTitle.setText(movies.getTitle());
        TextView tvOverview = holder.overview;
        tvOverview.setText(movies.getOverview());
        ImageView ivPoster = holder.poster;

        String imgUrl = movies.getPoster_path();

        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            imgUrl = movies.getBackdrop_path();
        }

        Glide.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(ivPoster);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}
