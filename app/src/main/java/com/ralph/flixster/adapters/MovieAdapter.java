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
import com.bumptech.glide.request.target.Target;
import com.ralph.flixster.R;
import com.ralph.flixster.models.Movies;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int BASIC = 0,POPULAR = 1;
    private List<Movies> moviesList;
    private Context context;

    public MovieAdapter(List<Movies> moviesList,Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(Double.valueOf(moviesList.get(position).getVote_average())>5.0){
            return POPULAR;
        }else{
            return BASIC;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case BASIC:
                View mView = inflater.inflate(R.layout.custom_row,parent,false);
                viewHolder = new ViewHolderBasic(mView);
                break;
            case POPULAR:
                View view = inflater.inflate(R.layout.custom_row_popular,parent,false);
                viewHolder = new ViewHolderPopular(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case BASIC:
                ViewHolderBasic viewHolderBasic = (ViewHolderBasic) holder;
                bindDatatoViewHolderBasic(viewHolderBasic,position);
                break;
            case POPULAR:
                ViewHolderPopular viewHolderPopular = (ViewHolderPopular) holder;
                bindDatatoViewHolderPopular(viewHolderPopular,position);
                break;
        }
    }

    private void bindDatatoViewHolderPopular(ViewHolderPopular viewHolderPopular, int position) {
        Movies movies = moviesList.get(position);
        ImageView ivBackdrop = viewHolderPopular.backdrop;

        String imgUrl = movies.getBackdrop_path();

        Glide.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(ivBackdrop);
    }

    private void bindDatatoViewHolderBasic(ViewHolderBasic holder, int position) {
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
                .override(Target.SIZE_ORIGINAL,200)
                .into(ivPoster);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    /*@NonNull
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
    }*/

    public class ViewHolderBasic extends RecyclerView.ViewHolder{

        public ImageView poster;
        public TextView title;
        public TextView overview;

        public ViewHolderBasic(View itemView) {
            super(itemView);

            poster = (ImageView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            overview = (TextView) itemView.findViewById(R.id.tvOverview);

        }
    }

    public class ViewHolderPopular extends RecyclerView.ViewHolder{

        public ImageView backdrop;

        public ViewHolderPopular(@NonNull View itemView) {
            super(itemView);

            backdrop = (ImageView) itemView.findViewById(R.id.ivBackdrop);

        }
    }

}
