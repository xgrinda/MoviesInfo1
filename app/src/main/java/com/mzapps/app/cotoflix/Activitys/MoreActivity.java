package com.mzapps.app.cotoflix.Activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mzapps.app.cotoflix.Adapter.MoviesAdapter;
import com.mzapps.app.cotoflix.BuildConfig;
import com.mzapps.app.cotoflix.Model.Movie;
import com.mzapps.app.cotoflix.Model.MoviesResponse;
import com.mzapps.app.cotoflix.R;
import com.mzapps.app.cotoflix.Utility.ApiClient;
import com.mzapps.app.cotoflix.Utility.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoreActivity extends AppCompatActivity {
    Context mContext;
    private AdView mAdView;
    RecyclerView fragment_grid_recylerview;
    String value;
    private boolean userScrolled = true;
    private static int  pageIndex ;
    private LinearLayoutManager mLayoutManager;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private  RelativeLayout bottomLayout;
    MoviesAdapter moviesAdapter;
    private List<Movie> movies = new ArrayList<Movie>();
    private List<Movie> movies2 = new ArrayList<Movie>();
    String TMBDB_API_KEY= BuildConfig.TMBDB_API_KEY;
    Handler mHandler;
    Runnable myRunnable;
    String BASE_URL = "https://api.themoviedb.org/3/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.more_activity);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mContext = this;
        fragment_grid_recylerview= findViewById(R.id.fragment_grid_recylerview);
        bottomLayout = findViewById(R.id.loadItemsLayout_recyclerView);
        mLayoutManager = new GridLayoutManager(mContext,3);
        fragment_grid_recylerview.setLayoutManager(mLayoutManager);
        pageIndex = 1;

        Intent intent = getIntent();
        value = intent.getStringExtra("value");

        if(myRunnable != null){
            mHandler.removeCallbacks(myRunnable);

        }
        retrofit();
        Pagination();


    }

    private void Pagination() {
        fragment_grid_recylerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView,
                                                     int newState) {

                        super.onScrollStateChanged(recyclerView, newState);

                        // If scroll state is touch scroll then set userScrolled
                        // true
                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                            userScrolled = true;

                        }
                    }
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx,
                                           int dy) {

                        super.onScrolled(recyclerView, dx, dy);
                        // Here get the child count, item count and visibleitems
                        // from layout manager

                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        pastVisiblesItems = mLayoutManager
                                .findFirstVisibleItemPosition();

                        // Now check if userScrolled is true and also check if
                        // the item is end then update recycler view and set
                        // userScrolled to false
                        if (userScrolled
                                && (visibleItemCount + pastVisiblesItems) == totalItemCount) {
                            userScrolled = false;
                            updateRecyclerView();
                        }
                    }

                });
    }

    private void updateRecyclerView() {
        bottomLayout.setVisibility(View.VISIBLE);
        mHandler=  new Handler();
        myRunnable = new Runnable() {
            public void run() {
                retrofit();

                bottomLayout.setVisibility(View.GONE);
            }
        };
        mHandler.postDelayed(myRunnable,2000);


    }


    private void retrofit() {



        //====================================================================================
        //====================================================================================
        //====================================================================================
        Call<MoviesResponse> call = getapiService();
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                pageIndex++;

                if(pageIndex >=2){
                    movies2 = response.body().getResults();
                    movies.addAll(movies2);
                    moviesAdapter = new MoviesAdapter(movies, mContext);
                    fragment_grid_recylerview.setAdapter(moviesAdapter);
                    fragment_grid_recylerview.scrollToPosition(pastVisiblesItems);

                }else {
                    movies = response.body().getResults();
                    moviesAdapter = new MoviesAdapter(movies, mContext);
                    fragment_grid_recylerview.setAdapter(moviesAdapter);
                }

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("tag", t.toString());
            }
        });
    }


    private Call<MoviesResponse> getapiService() {
        ApiInterface apiService = ApiClient.getClient(mContext,BASE_URL).create(ApiInterface.class);
        Call<MoviesResponse> calll = null;
        switch (value){
            case "more_Popular_tv":
                calll = apiService.getpopularTV(TMBDB_API_KEY,pageIndex);
                break;
            case "more_top_rated_tv":
                calll = apiService.getTopRatedTV(TMBDB_API_KEY,pageIndex);
                break;
            case "more_upcoming":
                calll = apiService.getupcomingMovies(TMBDB_API_KEY,pageIndex);
                break;
            case "more_top_rated":
                calll = apiService.getTopRatedMovies(TMBDB_API_KEY,pageIndex);
                break;
            case "more_Popular":
                calll = apiService.getpopularMovies(TMBDB_API_KEY,pageIndex);
                break;
        }
        return calll;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(myRunnable != null){
            mHandler.removeCallbacksAndMessages(myRunnable);
        }
    }
}

