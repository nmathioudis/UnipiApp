package com.example.nikos.unipiapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends DropDownMenu {

    ListView listView;
    FirebaseAuth mAuth;
    SearchView searchView;

    //String query = "greece";


    //Example
    ArrayList<NewsDataModel> newsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        newsArrayList = new ArrayList<>();


//        Toolbar toolbar;
        listView = findViewById(R.id.listNews);
        searchView = findViewById(R.id.SearchView);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNews(query);
                Log.e("QUERY", searchView.toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void latestNews() {
        // Retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Interface
        LatestNewsInterface latestNewsInterface = retrofit.create(LatestNewsInterface.class);

        latestNewsInterface.getNews().enqueue(new Callback<NewsDataModel>() {
            @Override
            public void onResponse(Call<NewsDataModel> call, Response<NewsDataModel> response) {
                if (response.isSuccessful()) {
                    NewsDataModel news = response.body();
                    Log.i("Status:", news.getStatus());
                    Log.i("TotalResult:", news.getTotalResults().toString());
                    for (int i = 0; i < news.getArticles().size(); i++) {
                        newsArrayList.add(new NewsDataModel(
                                        news.getStatus(),
                                        news.getTotalResults(),
                                        news.getArticles()
                                )
                        );

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                //--------- Start Cases ---------//
                                //First Case
                                String title = newsArrayList.get(position).getArticles().get(position).getTitle();
                                String content = newsArrayList.get(position).getArticles().get(position).getContent();
                                String name = newsArrayList.get(position).getArticles().get(position).getSource().getName();
                                String urlImage = newsArrayList.get(position).getArticles().get(position).getUrlToImage();
                                Bundle bundle = new Bundle();
                                bundle.putString("TITLE", title);
                                bundle.putString("CONTENT", content);
                                bundle.putString("NAME", name);
                                bundle.putString("URLIMAGE", urlImage);
                                Intent newsDisplayActivity = new Intent(MainActivity.this, NewsDisplayActivity.class);
                                newsDisplayActivity.putExtras(bundle);
                                startActivity(newsDisplayActivity);

                                //Second Case
//                                List<Article> setNewsList = newsArrayList.get(position).getArticles();
//                                Intent newsDisplayActivity = new Intent(MainActivity.this, NewsDisplayActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putParcelableArrayList("Articles", (ArrayList<? extends Parcelable>) setNewsList);
//                                bundle.putInt("Position", position);
//                                newsDisplayActivity.putExtras(bundle);
//                                startActivity(newsDisplayActivity);

                                //--------- Stop Cases ---------//

                                //Outpout
                                Log.d("Messege", newsArrayList.get(position).getArticles().get(position).toString());
                            }
                        });
//                        Log.i("Articles:", news.getArticles().get(i).getTitle().toString());
//                        Log.i("Articles:", news.getArticles().get(i).getUrlToImage().toString());
//                        newsArticles.add(news.getArticles().get(i).getTitle().toString());
//                        newsArticlesImagesUrl.add(news.getArticles().get(i).getUrlToImage().toString());
                    }
                } else {
                    Log.e("Failed:", "das" + response.code());
                }


                NewsListAdapter adapter = new NewsListAdapter(getApplicationContext(), R.layout.listview_layout, newsArrayList);
                listView.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<NewsDataModel> call, Throwable t) {
                Log.e("Failed error", "dasdasdsada" + t.getMessage());
            }
        });
    }


    private void getNews(String query) {
        // Retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newsArrayList.clear();
        //Interface
        NewsInterface newsInterface = retrofit.create(NewsInterface.class);

        newsInterface.getNews(query).enqueue(new Callback<NewsDataModel>() {
            @Override
            public void onResponse(Call<NewsDataModel> call, Response<NewsDataModel> response) {
                if (response.isSuccessful()) {
                    NewsDataModel news = response.body();
                    Log.i("Status:", news.getStatus());
                    Log.i("TotalResult:", news.getTotalResults().toString());
                    for (int i = 0; i < news.getArticles().size(); i++) {
                        newsArrayList.add(new NewsDataModel(
                                        news.getStatus(),
                                        news.getTotalResults(),
                                        news.getArticles()
                                )
                        );

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                String title = newsArrayList.get(position).getArticles().get(position).getTitle();
                                String content = newsArrayList.get(position).getArticles().get(position).getContent();
                                String name = newsArrayList.get(position).getArticles().get(position).getSource().getName();
                                String urlImage = newsArrayList.get(position).getArticles().get(position).getUrlToImage();
                                Bundle bundle = new Bundle();
                                bundle.putString("TITLE", title);
                                bundle.putString("CONTENT", content);
                                bundle.putString("NAME", name);
                                bundle.putString("URLIMAGE", urlImage);
                                Intent newsDisplayActivity = new Intent(MainActivity.this, NewsDisplayActivity.class);
                                newsDisplayActivity.putExtras(bundle);
                                startActivity(newsDisplayActivity);



                                //--------- Stop Cases ---------//

                                //Outpout
                                Log.d("Messege", newsArrayList.get(position).getArticles().get(position).toString());
                            }
                        });

                    }
                } else {
                    Log.e("Failed:", "das" + response.code());
                }


                NewsListAdapter adapter = new NewsListAdapter(getApplicationContext(), R.layout.listview_layout, newsArrayList);
                listView.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<NewsDataModel> call, Throwable t) {
                Log.e("Failed error", "dasdasdsada" + t.getMessage());
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        latestNews();
    }

}
