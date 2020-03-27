package com.mzapps.app.cotoflix.Activitys;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mzapps.app.cotoflix.Adapter.MoviesAdapter;
import com.mzapps.app.cotoflix.BuildConfig;
import com.mzapps.app.cotoflix.Model.Actor;
import com.mzapps.app.cotoflix.Model.ActorMovies;
import com.mzapps.app.cotoflix.Model.Movie;
import com.mzapps.app.cotoflix.R;
import com.mzapps.app.cotoflix.Utility.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ActorActivity extends AppCompatActivity {
    Context mContext;
    public  static final String TAG_ACTOR_FRAGMENT = "TAG_ACTOR_FRAGMENT";
    String ROOT_URL = "https://api.themoviedb.org/3/";
    private AdView mAdView;
    int person_id;
    String TMBDB_API_KEY= BuildConfig.TMBDB_API_KEY;
    TextView title,year,id_number_actor,place_of_birth_actor,biography;
    ImageView cover;
    RecyclerView recyclerView_movies_actor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.actor_activity);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/brownregular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mContext = this;

        title = findViewById(R.id.title);
        year = findViewById(R.id.year);
        id_number_actor = findViewById(R.id.id_number_actor);
        cover = findViewById(R.id.cover);
        place_of_birth_actor = findViewById(R.id.place_of_birth_actor);
        biography = findViewById(R.id.biography);
        recyclerView_movies_actor = findViewById(R.id.recyclerView_movies_actor);


        person_id = getIntent().getExtras().getInt("person_id");

        Retrofit_call();
    }


    private void Retrofit_call() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        api.getActorDetails(person_id,TMBDB_API_KEY).enqueue(new retrofit2.Callback<Actor>() {
            @Override
            public void onResponse(Call<Actor> call, Response<Actor> response) {
                try{
                title.setText(response.body().getName());
                year.setText(response.body().getBirthday().substring(0,4));
                id_number_actor.setText("ID: "+response.body().getId());
                place_of_birth_actor.setText(response.body().getPlaceOfBirth());
                biography.setText(response.body().getBiography());

                Glide.with(mContext)
                        .load("http://image.tmdb.org/t/p/w780"+response.body().getProfilePath())
                        .into(cover);


                    Bundle bundle = new Bundle();
                    bundle.putString("actor", response.body().getName());
                }catch (Exception e){
                    Log.e("TAG", "Unable to submit post to API.");
                }
            }
            @Override
            public void onFailure(Call<Actor> call, Throwable t) {
                Log.e("TAG", "Unable to submit post to API.");
            }
        });

        api.getActorMovies(person_id,TMBDB_API_KEY).enqueue(new retrofit2.Callback<ActorMovies>() {
            @Override
            public void onResponse(Call<ActorMovies> call, Response<ActorMovies> response) {
                try{
                    recyclerView_movies_actor.setLayoutManager(new GridLayoutManager(mContext,3,LinearLayoutManager.VERTICAL,true));
                    List<Movie> movies = response.body().getCast();
                    recyclerView_movies_actor.setAdapter(new MoviesAdapter(movies, mContext));
                    recyclerView_movies_actor.setNestedScrollingEnabled(false);

                }catch (Exception e){
                    Log.e("TAG", "Unable to submit post to API.");
                }
            }
            @Override
            public void onFailure(Call<ActorMovies> call, Throwable t) {
                Log.e("TAG", "Unable to submit post to API.");
            }
        });


    }




}
