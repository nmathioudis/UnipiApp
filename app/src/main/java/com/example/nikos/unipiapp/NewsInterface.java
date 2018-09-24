package com.example.nikos.unipiapp;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface NewsInterface {

    String BASE_URL = "https://newsapi.org/v2/";

    @GET("everything?apiKey=53e8c365cc624c1288254b1f9402f6ff&x")
    Call<NewsDataModel> getNews(
            @Query("q") String q);
}
