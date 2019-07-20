package com.ralph.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.sax.TextElementListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ralph.flixster.models.Movies;

import org.parceler.Parcels;

import java.util.Date;

public class DetailsActivity extends AppCompatActivity {

    Movies movies;
    ImageView ivShowPoster;
    TextView tvShowTitle;
    TextView tvShowOverview;
    TextView tvShowPopularity;
    TextView tvShowReleaseDate;
    ImageView trailerPic;
    RatingBar ratingBar;
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
        tvShowTitle = (TextView) findViewById(R.id.tvShowTitle);
        tvShowOverview = (TextView) findViewById(R.id.tvShowOverview);
        ivShowPoster = (ImageView) findViewById(R.id.ivShowPoster);
        tvShowPopularity = (TextView) findViewById(R.id.tvShowPopularity);
        tvShowReleaseDate = (TextView) findViewById(R.id.tvShowReleaseDate);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        trailerPic =  (ImageView) findViewById(R.id.imgDetails);

        movies = (Movies) Parcels.unwrap(getIntent().getParcelableExtra("movies"));
        tvShowTitle.setText(movies.getTitle());
        tvShowOverview.setText(movies.getOverview());
        tvShowPopularity.setText(movies.getPopularity());
        
        tvShowReleaseDate.setText(movies.getRelease_date());
        ratingBar.setRating((float) Double.parseDouble(movies.getVote_average()));

        Glide.with(this).load(movies.getPoster_path())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.error))
                .into(ivShowPoster);
        Glide.with(this).load(movies.getBackdrop_path())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.error))
                .into(trailerPic);

    }
}
