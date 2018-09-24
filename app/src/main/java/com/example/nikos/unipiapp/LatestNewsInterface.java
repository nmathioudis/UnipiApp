package com.example.nikos.unipiapp;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LatestNewsInterface {

    String BASE_URL = "https://newsapi.org/v2/";

    @GET("top-headlines?country=us&apiKey=53e8c365cc624c1288254b1f9402f6ff&x")
    Call<NewsDataModel> getNews();
//    List<NewsDataModel> news = (List<NewsDataModel>) new Gson().fromJson();
}
