package com.ralph.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movies {
    String poster_path;
    String title;
    String overview;
    String backdrop_path;
    String vote_average;

    public Movies(JSONObject jsonObject) throws JSONException {
        poster_path = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdrop_path = jsonObject.getString("backdrop_path");
        vote_average = jsonObject.getString("vote_average");

    }

    public static List<Movies> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Movies> list = new ArrayList<>();
        for(int i=0; i < jsonArray.length(); i++){
            list.add(new Movies(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getBackdrop_path() {
        return String.format("https://image.tmdb.org/t/p/w1280/%s", backdrop_path);
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
}
