package com.mzapps.app.cotoflix.Fragments.MainFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mzapps.app.cotoflix.Adapter.MoviesAdapter;
import com.mzapps.app.cotoflix.Activitys.MoreActivity;
import com.mzapps.app.cotoflix.BuildConfig;
import com.mzapps.app.cotoflix.Model.Movie;
import com.mzapps.app.cotoflix.Model.MoviesResponse;
import com.mzapps.app.cotoflix.R;
import com.mzapps.app.cotoflix.Utility.ApiClient;
import com.mzapps.app.cotoflix.Utility.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mzapps.app.cotoflix.Fragments.MainFragment.HomeFragment.apiService;


public class TvShowsFragment extends Fragment implements View.OnClickListener{
    RecyclerView movies_recycler4_tv_popular,movies_recycler5_tv_toprated;
    ProgressBar progressBar;
    Context mContext;
    LinearLayout layout;
    String TMBDB_API_KEY = BuildConfig.TMBDB_API_KEY;
    TextView more_Popular_tv,more_top_rated_tv;
    String BASE_URL = "https://api.themoviedb.org/3/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tvshowsfragment, container, false);
        mContext = this.getContext();
        movies_recycler4_tv_popular =  view.findViewById(R.id.movies_recycler4_tv_popular);
        movies_recycler5_tv_toprated =  view.findViewById(R.id.movies_recycler5_tv_toprated);

        more_Popular_tv=  view.findViewById(R.id.more_Popular_tv);
        more_top_rated_tv=  view.findViewById(R.id.more_top_rated_tv);


        more_Popular_tv.setOnClickListener(this);
        more_top_rated_tv.setOnClickListener(this);

        progressBar =  view.findViewById(R.id.progressBar_tv);
        layout =  view.findViewById(R.id.layout_tv);

        movies_recycler4_tv_popular.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        movies_recycler5_tv_toprated.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        retrofit();
        return view;
    }


    private void retrofit() {
        apiService = ApiClient.getClient(mContext,BASE_URL).create(ApiInterface.class);

        //====================================================================================
        //====================================================================================
        //====================================================================================
        Call<MoviesResponse> call_popularTV = apiService.getpopularTV(TMBDB_API_KEY,1);
        call_popularTV.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse>call, Response<MoviesResponse> response) {
                try{
                    List<Movie> movies = response.body().getResults();
                    movies_recycler4_tv_popular.setAdapter(new MoviesAdapter(movies, mContext));
                    progressBar.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                }catch (Exception e){ }
            }

            @Override
            public void onFailure(Call<MoviesResponse>call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
            }
        });



        //====================================================================================
        //====================================================================================
        //====================================================================================
        Call<MoviesResponse> call_getTopRatedTV = apiService.getTopRatedTV(TMBDB_API_KEY,1);
        call_getTopRatedTV.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse>call, Response<MoviesResponse> response) {

                try{
                    List<Movie> movies = response.body().getResults();
                    movies_recycler5_tv_toprated.setAdapter(new MoviesAdapter(movies, mContext));
                }catch (Exception e)
                { Log.e("TAG", e.toString());   }
            }
            @Override
            public void onFailure(Call<MoviesResponse>call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more_Popular_tv:
                switchActivity("more_Popular_tv");
                break;
            case R.id.more_top_rated_tv:
                switchActivity("more_top_rated_tv");

                break;
        }
    }


    private void switchActivity(String value) {
        Intent intent = new Intent(getActivity(), MoreActivity.class);
        intent.putExtra("value",value);
        startActivity(intent);

    }


}