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

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.mzapps.app.cotoflix.Adapter.MoviesAdapter;
import com.mzapps.app.cotoflix.Activitys.MoreActivity;
import com.mzapps.app.cotoflix.BuildConfig;
import com.mzapps.app.cotoflix.MainActivity;
import com.mzapps.app.cotoflix.Model.Movie;
import com.mzapps.app.cotoflix.Model.MoviesResponse;
import com.mzapps.app.cotoflix.Model.Slider;
import com.mzapps.app.cotoflix.R;
import com.mzapps.app.cotoflix.Utility.ApiClient;
import com.mzapps.app.cotoflix.Utility.ApiInterface;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class HomeFragment extends Fragment implements View.OnClickListener{

    private static String TMBDB_API_KEY = BuildConfig.TMBDB_API_KEY;
    RecyclerView movies_recycler1_UpComing,movies_recycler2_popular,movies_recycler3_top_rated;
    private Context mContext;
    SliderLayout mDemoSlider;
    public static  ProgressBar progressBar;
    public static  ApiInterface apiService;
    TextView more_upcoming,more_Popular,more_top_rated;
    private AdView mAdView;

    public  static  LinearLayout layout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mContext = this.getActivity();
        movies_recycler1_UpComing=  view.findViewById(R.id.movies_recycler1_UpComing);
        movies_recycler2_popular=  view.findViewById(R.id.movies_recycler2_popular);
        movies_recycler3_top_rated=  view.findViewById(R.id.movies_recycler3_top_rated);
        progressBar =  view.findViewById(R.id.progressBar);
        layout =  view.findViewById(R.id.layout);

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        more_upcoming= view.findViewById(R.id.more_upcoming);
        more_Popular= view.findViewById(R.id.more_Popular);
        more_top_rated= view.findViewById(R.id.more_top_rated);

        more_upcoming.setOnClickListener(this);
        more_Popular.setOnClickListener(this);
        more_top_rated.setOnClickListener(this);



        mDemoSlider = view.findViewById(R.id.slider);

        movies_recycler1_UpComing.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        movies_recycler2_popular.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        movies_recycler3_top_rated.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        retrofit();
        return view;
    }


    private void setupslider(final List<Slider> sliders) {


        for(final Slider s : sliders){
            TextSliderView textSliderView = new TextSliderView(mContext);
            // initialize a SliderLayout
            textSliderView
                    //.description(s.getTitle())
                    .image(s.getPosterPath())
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle();
                    //.putString("extra",name);

            textSliderView.image(s.getPosterPath())
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {

                }
            });
            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);

    }
    private void retrofit(){

        String BASE_URL = "https://api.themoviedb.org/3/";

        apiService = ApiClient.getClient(mContext,BASE_URL).create(ApiInterface.class);

        Call<MoviesResponse> call_UpComing = apiService.getupcomingMovies(TMBDB_API_KEY,1);
        call_UpComing.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse>call, Response<MoviesResponse> response) {
                try{
                    List<Movie> movies = response.body().getResults();
                    movies_recycler1_UpComing.setAdapter(new MoviesAdapter(movies, mContext));
                    Log.e("TAG", "null array");
                }catch (Exception e){      }
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
        Call<MoviesResponse> call_popular = apiService.getpopularMovies(TMBDB_API_KEY,1);
        call_popular.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse>call, Response<MoviesResponse> response) {

                try{
                    List<Movie> movies = response.body().getResults();
                    movies_recycler2_popular.setAdapter(new MoviesAdapter(movies, mContext));
                }catch (Exception e)
                {
                    Log.e("TAG", e.toString());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse>call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });
        //====================================================================================
        //====================================================================================
        //====================================================================================
        Call<MoviesResponse> call_top_rated = apiService.getTopRatedMovies(TMBDB_API_KEY,1);
        call_top_rated.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse>call, Response<MoviesResponse> response) {


                try{
                    List<Movie> movies = response.body().getResults();
                    movies_recycler3_top_rated.setAdapter(new MoviesAdapter(movies, mContext));
                }catch (Exception e)
                {
                    Log.e("TAG", e.toString());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse>call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });

        //====================================================================================
        //====================================================================================
        //====================================================================================

        Call<List<Slider>> call_slider = apiService.getSlider("https://mohamedebrahim.000webhostapp.com/plex/slider.php");
        call_slider.enqueue(new Callback<List<Slider>>() {
            @Override
            public void onResponse(Call<List<Slider>>call, Response<List<Slider>> response) {
                try {
                    List<Slider> slider = response.body();
                    Log.i("slider", slider.get(0).getTitle());
                    setupslider(slider);
                    progressBar.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                }catch (Exception e ){
                    Log.e("Exception::",e.toString());
                }

            }

            @Override
            public void onFailure(Call<List<Slider>>call, Throwable t) {
                // Log error here since request failed
                Log.e("slider", t.toString());
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more_upcoming:
                moreActivity("more_upcoming");
                //ShowInterstitial();
                break;
            case R.id.more_top_rated:
                moreActivity("more_top_rated");
                //ShowInterstitial();
                break;
            case R.id.more_Popular:
                moreActivity("more_Popular");
                //ShowInterstitial();
                break;
        }
    }

    private void moreActivity(String value) {
        Intent intent = new Intent(getActivity(), MoreActivity.class);
        intent.putExtra("value",value);
        startActivity(intent);

    }

    private void ShowInterstitial(){
        if (MainActivity.mInterstitialAd.isLoaded()){
            MainActivity.mInterstitialAd.show();
        }
    }


}
