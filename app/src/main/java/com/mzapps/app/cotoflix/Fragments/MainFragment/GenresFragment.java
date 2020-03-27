package com.mzapps.app.cotoflix.Fragments.MainFragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mzapps.app.cotoflix.Adapter.GenreAdapter;
import com.mzapps.app.cotoflix.BuildConfig;
import com.mzapps.app.cotoflix.Model.Genre;
import com.mzapps.app.cotoflix.Model.GenreResponse;
import com.mzapps.app.cotoflix.R;
import com.mzapps.app.cotoflix.Utility.ApiClient;
import com.mzapps.app.cotoflix.Utility.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mzapps.app.cotoflix.Fragments.MainFragment.HomeFragment.apiService;

/**
 * Created by XgRiNdA on 04,August,2019
 */

public class GenresFragment extends Fragment  {
    Context mContext;
    Handler mHandler;
    Runnable myRunnable;
    RecyclerView recyclerView_genres;
    private List<Genre> genres = new ArrayList<Genre>();
    String TMBDB_API_KEY = BuildConfig.TMBDB_API_KEY;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.genre_fragment,container,false);
        mContext = this.getContext();
        recyclerView_genres = (RecyclerView)view.findViewById(R.id.recyclerView_genre);
        recyclerView_genres.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        //mLayoutManager = new GridLayoutManager(mContext,3);
        //recyclerView_genres.setLayoutManager(mLayoutManager);
        retrofit();
        return view;
    }

    private void retrofit() {

        String BASE_URL = "https://api.themoviedb.org/3/";
        apiService = ApiClient.getClient(mContext, BASE_URL).create(ApiInterface.class);

        Call<GenreResponse> call_recyclerView_actors = apiService.getGenres(TMBDB_API_KEY);
        call_recyclerView_actors.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                try {

                    genres = response.body().getResults();
                    recyclerView_genres.setAdapter(new GenreAdapter(genres,mContext));

                }catch (Exception e ){
                    Log.e("Exception::",e.toString());
                }

            }
            @Override
            public void onFailure(Call<GenreResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("TAG", "Stop handler ");
        if (myRunnable != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.removeCallbacks(myRunnable);
        }
    }

}
