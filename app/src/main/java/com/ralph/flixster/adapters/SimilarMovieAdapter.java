package com.ralph.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ralph.flixster.DetailsActivity;
import com.ralph.flixster.R;
import com.ralph.flixster.models.Movies;

import org.parceler.Parcels;

import java.util.List;

public class SimilarMovieAdapter extends RecyclerView.Adapter<SimilarMovieAdapter.ViewHolder> {

    private List<Movies> moviesList;
    private Context context;

    public SimilarMovieAdapter(List<Movies> list, Context context) {
        this.moviesList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View similarView = inflater.inflate(R.layout.similar_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(similarView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movies movies = moviesList.get(position);
        ImageView imageView = holder.ivPoster;

        String imgUrl = movies.getPoster_path();

        Glide.with(context)
                .load(imgUrl)
                .apply(new RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.error))
                .apply(new RequestOptions().transforms(new CenterInside(),new RoundedCorners(30)))
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("movies", Parcels.wrap(movies));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPoster = (ImageView) itemView.findViewById(R.id.ivPosterSimilar);

        }
    }
}
