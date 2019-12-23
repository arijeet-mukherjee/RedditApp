package com.arijeet.newsapp;

import com.arijeet.newsapp.models.Feed;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {
    String BASE_URL = "https://www.reddit.com/r/";
//non-static feed name
    @GET("{feed_name}/.rss")
    Call<Feed> getFeed(@Path("feed_name") String feed_name);

   // @GET("earthporn/.rss")
    //Call<Feed> getFeed();
}
