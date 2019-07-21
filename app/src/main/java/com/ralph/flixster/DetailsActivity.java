package com.ralph.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.ralph.flixster.adapters.SimilarMovieAdapter;
import com.ralph.flixster.models.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class DetailsActivity extends AppCompatActivity {

    private static final String TRAILERS_API = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    List<Movies> list;
    Movies movies;
    ImageView ivShowPoster;
    //FrameLayout trailerContainer;
    ImageView playButton;
    RecyclerView rvSimilar;
    TextView tvShowTitle;
    TextView tvShowOverview;
    TextView tvShowPopularity;
    TextView tvShowReleaseDate;
    ImageView trailerPic;
    RatingBar ratingBar;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.toolbarDetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //declare views
        //ButterKnife.bind(this);
        tvShowTitle = (TextView) findViewById(R.id.tvShowTitle);
        tvShowOverview = (TextView) findViewById(R.id.tvShowOverview);
        ivShowPoster = (ImageView) findViewById(R.id.ivShowPoster);
        tvShowPopularity = (TextView) findViewById(R.id.tvShowPopularity);
        tvShowReleaseDate = (TextView) findViewById(R.id.tvShowReleaseDate);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        //trailerContainer = (FrameLayout) findViewById(R.id.trailer);
        playButton = (ImageView) findViewById(R.id.play);

        trailerPic =  (ImageView) findViewById(R.id.imgDetails);


        movies = (Movies) Parcels.unwrap(getIntent().getParcelableExtra("movies"));
        tvShowTitle.setText(movies.getTitle());
        tvShowOverview.setText(movies.getOverview());
        tvShowPopularity.setText(movies.getPopularity());
        id = movies.getId();
        tvShowReleaseDate.setText(movies.getRelease_date());
        ratingBar.setRating((float) Double.parseDouble(movies.getVote_average()));


        Glide.with(DetailsActivity.this).load(movies.getPoster_path())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.error))
                .into(ivShowPoster);
        Glide.with(this).load(movies.getBackdrop_path())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.error))
                .into(trailerPic);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(TRAILERS_API, Integer.parseInt(id)), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("results");
                    if(array.length()==0){
                        return;
                    }
                    JSONObject jsonObject = array.getJSONObject(0);
                    final String key = jsonObject.getString("key");

                    playButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(DetailsActivity.this,YoutubePlayerActivity.class);
                            intent.putExtra("key",key);
                            startActivity(intent);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        initSimilarMovies();
    }

    private void initSimilarMovies() {
        list = new ArrayList<>();

        rvSimilar = findViewById(R.id.rvSimilar);
        final SimilarMovieAdapter adapter = new SimilarMovieAdapter(list,this);
        rvSimilar.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        rvSimilar.setAdapter(adapter);

        String url = String.format("https://api.themoviedb.org/3/movie/%s/similar?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US", id);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray array = null;
                try {
                    array = response.getJSONArray("results");
                    list.addAll(Movies.fromJsonArray(array));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
