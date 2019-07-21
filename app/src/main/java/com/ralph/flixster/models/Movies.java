package com.ralph.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movies {
    String id;
    String poster_path;
    String title;
    String overview;
    String backdrop_path;
    String vote_average;
    String popularity;
    String release_date;

    public Movies(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getString("id");
        poster_path = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdrop_path = jsonObject.getString("backdrop_path");
        vote_average = jsonObject.getString("vote_average");
        popularity = jsonObject.getString("popularity");
        release_date = jsonObject.getString("release_date");

    }

    public Movies(){

    }

    public static List<Movies> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Movies> list = new ArrayList<>();
        for(int i=0; i < jsonArray.length(); i++){
            list.add(new Movies(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    public String getId() {
        return id;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getBackdrop_path() {
        return String.format("https://image.tmdb.org/t/p/w780/%s", backdrop_path);
    }

    public String getPoster_path() {
        return String.format("https://image.tmdb.org/t/p/w342/%s",poster_path);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getRelease_date() {
        return release_date;
    }
}
