package com.ralph.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.ralph.flixster.DetailsActivity;
import com.ralph.flixster.MoviesActivity;
import com.ralph.flixster.R;
import com.ralph.flixster.YoutubePlayerActivity;
import com.ralph.flixster.models.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TRAILERS_API = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private final int BASIC = 0,POPULAR = 1;
    private List<Movies> moviesList;
    private Context context;
    private int radius = 30;

    public MovieAdapter(List<Movies> moviesList,Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(Double.valueOf(moviesList.get(position).getVote_average())>5.0){
            return POPULAR;
        }else {
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
        final Movies movies = moviesList.get(position);
        ImageView ivBackdrop = viewHolderPopular.backdrop;
        FrameLayout layout = viewHolderPopular.layout;

        TextView tvTitle = viewHolderPopular.tvPopTitle;
        tvTitle.setText(movies.getTitle());
        TextView tvOverview = viewHolderPopular.tvPopOverview;
        tvOverview.setText(movies.getOverview());

        String imgUrl = movies.getBackdrop_path();
        Glide.with(context)
                .load(imgUrl)
                .apply(new RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.error))
                .apply(new RequestOptions().transforms(new CenterInside(),new RoundedCorners(radius)))
                .into(ivBackdrop);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FetchTrailerKey
                Log.i("IdAdapter",movies.getId());
                fetchTrailerKey(movies.getId());
            }
        });
        //when long clicked show details
        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("movies", Parcels.wrap(movies));
                context.startActivity(intent);
                return true;
            }
        });
    }

    private void fetchTrailerKey(String id) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(TRAILERS_API,Integer.parseInt(id)),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("results");
                    if(array.length()==0){
                        return;
                    }
                    JSONObject jsonObject = array.getJSONObject(0);
                    String trailer_key = jsonObject.getString("key");
                    Log.i("KeyAdapter","key "+trailer_key);

                    Intent intent = new Intent(context,YoutubePlayerActivity.class);
                    intent.putExtra("key",trailer_key);
                    context.startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Error : "+e,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void bindDatatoViewHolderBasic(ViewHolderBasic holder, int position) {
        final Movies movies = moviesList.get(position);
        TextView tvTitle = holder.title;
        tvTitle.setText(movies.getTitle());
        TextView tvOverview = holder.overview;
        tvOverview.setText(movies.getOverview());
        ImageView ivPoster = holder.poster;

        String imgUrl = movies.getPoster_path();

        Glide.with(context)
                .load(imgUrl)
                .apply(new RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.error))
                .apply(new RequestOptions().transforms(new CenterInside(),new RoundedCorners(radius)))
                .into(ivPoster);

        RelativeLayout relativeLayout = holder.container;
        relativeLayout.setOnClickListener(new View.OnClickListener() {
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

    public class ViewHolderBasic extends RecyclerView.ViewHolder{

        public ImageView poster;
        public TextView title;
        public TextView overview;
        public RelativeLayout container;

        public ViewHolderBasic(View itemView) {
            super(itemView);

            poster = (ImageView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            overview = (TextView) itemView.findViewById(R.id.tvOverview);
            container = (RelativeLayout) itemView.findViewById(R.id.container);

        }
    }

    public class ViewHolderPopular extends RecyclerView.ViewHolder{

        public ImageView backdrop;
        public TextView tvPopTitle;
        public TextView tvPopOverview;
        public FrameLayout layout;

        public ViewHolderPopular(@NonNull View itemView) {
            super(itemView);

            backdrop = (ImageView) itemView.findViewById(R.id.ivBackdrop);
            tvPopTitle = (TextView) itemView.findViewById(R.id.tvPopTitle);
            tvPopOverview = (TextView) itemView.findViewById(R.id.tvPopOverview);
            layout = (FrameLayout) itemView.findViewById(R.id.trailerPopularLayout);

        }
    }

}
