package com.mzapps.app.cotoflix.Activitys;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mzapps.app.cotoflix.Adapter.CreditsAdapter;
import com.mzapps.app.cotoflix.Adapter.CrewAdapter;
import com.mzapps.app.cotoflix.Adapter.ImagesAdapter;
import com.mzapps.app.cotoflix.Adapter.TrailerAdapter;
import com.mzapps.app.cotoflix.BuildConfig;
import com.mzapps.app.cotoflix.Model.Backdrop;
import com.mzapps.app.cotoflix.Model.Cast;
import com.mzapps.app.cotoflix.Model.Credits;
import com.mzapps.app.cotoflix.Model.Crew;
import com.mzapps.app.cotoflix.Model.Genre;
import com.mzapps.app.cotoflix.Model.Images_tmdb;
import com.mzapps.app.cotoflix.Model.Movie;
import com.mzapps.app.cotoflix.Model.MovieDetails;
import com.mzapps.app.cotoflix.Model.Trailer;
import com.mzapps.app.cotoflix.Model.TrailerResponse;
import com.mzapps.app.cotoflix.R;
import com.mzapps.app.cotoflix.Utility.ApiClient;
import com.mzapps.app.cotoflix.Utility.ApiInterface;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class DetailsMovie_Activity extends AppCompatActivity{

    private static boolean LIKE = true;
    private AdView mAdView;
    public static final String TAG_DetailsMovie_Fragment = "TAG_DetailsMovie_Fragment";

    TextView title,year,rating,overview,runtime,voteCount,genre1,genre2,genre3,id_number;
    ImageView cover;
    Context mContext;
    RecyclerView recyclerView_trailers,recyclerView_actors,recyclerView_crew,recyclerView_images;
    RelativeLayout layout_genre1,layout_genre2,layout_genre3;
    LinearLayout trailer_layout,actors_layout,crew_layout,image_layout;
    RatingBar ratingBar;
    ImageView like,watch;
    int x,year_balance;
    Movie movie;
    Handler mHandler;
    Runnable myRunnable;
    ApiInterface apiService;
    private String TMBDB_API_KEY = BuildConfig.TMBDB_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.details_movie_activity_layout);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mContext = this;
        title = findViewById(R.id.title);
        year = findViewById(R.id.year);
        rating = findViewById(R.id.rating);
        overview = findViewById(R.id.overview);
        runtime = findViewById(R.id.runtime);
        cover = findViewById(R.id.cover);
        ratingBar = findViewById(R.id.ratingBar);
        voteCount = findViewById(R.id.voteCount);
        genre1 = findViewById(R.id.genre1);
        genre2 = findViewById(R.id.genre2);
        genre3 = findViewById(R.id.genre3);
        id_number = findViewById(R.id.id_number);
        layout_genre1 = findViewById(R.id.layout_genre1);
        layout_genre2 = findViewById(R.id.layout_genre2);
        layout_genre3 = findViewById(R.id.layout_genre3);
        like = findViewById(R.id.like);
        watch = findViewById(R.id.watch);
        trailer_layout = findViewById(R.id.trailer_layout);
        actors_layout = findViewById(R.id.actors_layout);
        image_layout = findViewById(R.id.image_layout);

        recyclerView_trailers = findViewById(R.id.recyclerView_trailers);
        recyclerView_actors = findViewById(R.id.recyclerView_actors);
        recyclerView_images = findViewById(R.id.recyclerView_images);
        recyclerView_crew = findViewById(R.id.recyclerView_crew);
        crew_layout = findViewById(R.id.crew_layout);


        //like.setOnClickListener(this);

        movie = (Movie)getIntent().getExtras().getSerializable("movie");



        try{
            title.setText(movie.getOriginalTitle());
            year.setText(movie.getReleaseDate().substring(0, 4));
            rating.setText(movie.getVoteAverage().toString());
            overview.setText(movie.getOverview());
            ratingBar.setRating(movie.getVoteAverage().floatValue()/2);
            voteCount.setText(movie.getVoteCount().toString());
            x= movie.getId();
            id_number.setText("ID:" + String.valueOf(x));
            String cover_string_link ;
            if(movie.getBackdropPath() == null){
                cover_string_link = "http://image.tmdb.org/t/p/w780"+movie.getPosterPath().toString();
            }else {
                cover_string_link = "http://image.tmdb.org/t/p/w780"+movie.getBackdropPath().toString();
            }
            Glide.with(this)
                    .load(cover_string_link)
                    .transition(withCrossFade())
                    .into(cover);
            year_balance =  Integer.parseInt(movie.getReleaseDate().substring(0, 4));
        }catch (Exception e){
            Log.i("TAG :",e.toString());
        }
        retrofit();

    }

    private void retrofit() {

        String BASE_URL = "https://api.themoviedb.org/3/";
        apiService =
                ApiClient.getClient(mContext,BASE_URL).create(ApiInterface.class);

        Call<MovieDetails> call_details = apiService.getMovieDetails(x,TMBDB_API_KEY);
        call_details.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                MovieDetails m = response.body();


                    try{
                        int t = m.getRuntime();
                        int hours = t / 60; //since both are ints, you get an int
                        int minutes = t % 60;
                        System.out.printf("%d:%02d", hours, minutes);
                        if(hours != 0)
                            runtime.setText(String.valueOf(hours)+"h "+String.valueOf(minutes)+"min");
                        else
                            runtime.setText(String.valueOf(minutes)+"min");

                        setgenre(m.getGenres(),m.getGenres().size());
                    }catch (Exception e){
                        Log.i("TAG :",e.toString());
                    }

                //============================++++++++++++++++++++++++++

            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
            }
        });

        //setRecyclerView_trailers
        //==================================================================
        Call<TrailerResponse> call_RecyclerView_trailers = apiService.gettrailers("movie",x,TMBDB_API_KEY);
        call_RecyclerView_trailers.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                List<Trailer> trailers = null;
                try {
                    trailers = response.body().getResults();
                    if (response.body().getResults() != null)
                    {
                        trailer_layout.setVisibility(View.VISIBLE);
                        recyclerView_trailers.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                        recyclerView_trailers.setAdapter(new TrailerAdapter(trailers, mContext));


                    }
                }catch (Exception e ){
                    Log.e("Exception::",e.toString());
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
            }
        });

        //setRecyclerView_actors
        //==================================================================
        Call<Credits> call_recyclerView_actors = apiService.getMovieCredits(x,TMBDB_API_KEY);
        call_recyclerView_actors.enqueue(new Callback<Credits>() {
            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {
                List<Cast> casts = null;
                try {
                    casts = response.body().getCast();
                    if (casts != null)
                    {
                        actors_layout.setVisibility(View.VISIBLE);
                        recyclerView_actors.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                        recyclerView_actors.setAdapter(new CreditsAdapter(casts, mContext));

                    }
                }catch (Exception e ){
                    Log.e("Exception::>>",e.toString());
                }
            }

            @Override
            public void onFailure(Call<Credits> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });


        //setRecyclerView_actors
        //==================================================================
        Call<Credits> call_recyclerView_crew = apiService.getMovieCredits(x,TMBDB_API_KEY);
        call_recyclerView_crew.enqueue(new Callback<Credits>() {
            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {
                List<Crew> crews = null;
                try {
                    crews = response.body().getCrew();
                    if (response.body().getCrew() != null)
                    {
                        crew_layout.setVisibility(View.VISIBLE);
                        recyclerView_crew.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                        recyclerView_crew.setAdapter(new CrewAdapter(crews, mContext));

                    }
                }catch (Exception e ){
                    Log.e("Exception::",e.toString());
                }
            }

            @Override
            public void onFailure(Call<Credits> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });

        call_Retrofit_images();
    }
    private void call_Retrofit_images() {
        //setRecyclerView_images
        //==================================================================
        Call<Images_tmdb> call_RecyclerView_images = apiService.getImages("movie",x,TMBDB_API_KEY);
        call_RecyclerView_images.enqueue(new Callback<Images_tmdb>() {
            @Override
            public void onResponse(Call<Images_tmdb> call, Response<Images_tmdb> response) {
                //List<Poster> posters;
                List<Backdrop> posters = null;
                try {
                    posters = response.body().getBackdrops();
                    image_layout.setVisibility(View.VISIBLE);
                    recyclerView_images.setLayoutManager(new LinearLayoutManager(mContext,
                            LinearLayoutManager.HORIZONTAL, false));
                    recyclerView_images.setAdapter(new ImagesAdapter(posters, mContext));
                    if (posters.size() > 0) {
                        change_backdrop(posters);
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onFailure(Call<Images_tmdb> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
            }
        });
    }
    private void change_backdrop(final List<Backdrop> imageArray) {
        mHandler=  new Handler();
        myRunnable = new Runnable() {
            int i = 0;

            public void run() {
                Glide.with(mContext)
                        .load("http://image.tmdb.org/t/p/w780"+imageArray.get(i).getFilePath())
                        .transition(withCrossFade())
                        .apply(new RequestOptions().placeholder(cover.getDrawable()))
                        .into(cover);
                Log.e("TAG","change image bacrfull");
                i++;
                if (i > imageArray.size() - 1) {
                    i = 0;
                }
                mHandler.postDelayed(this, 4000);
            }
        };
        mHandler.postDelayed(myRunnable, 2000);
    }
    public void setgenre(List<Genre> names, int size){

        //Log.e("size",String.valueOf(size));

        if (size==1){
            genre1.setText(String.valueOf(names.get(0).getName()));
            layout_genre1.setVisibility(View.VISIBLE);

        }else if(size ==2){
            genre1.setText(String.valueOf(names.get(0).getName()));
            layout_genre1.setVisibility(View.VISIBLE);
            genre2.setText(String.valueOf(names.get(1).getName()));
            layout_genre2.setVisibility(View.VISIBLE);

        }else if(size >= 3) {
            genre1.setText(String.valueOf(names.get(0).getName()));
            layout_genre1.setVisibility(View.VISIBLE);
            genre2.setText(String.valueOf(names.get(1).getName()));
            layout_genre2.setVisibility(View.VISIBLE);
            genre3.setText(String.valueOf(names.get(2).getName()));
            layout_genre3.setVisibility(View.VISIBLE);
        }
    }

/*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.like:
                        if (LIKE){
                            like.setImageResource(R.drawable.if_heart_119_111093);
                            LIKE = false;
                        }else {
                            like.setImageResource(R.drawable.if_heart_1814104);
                            LIKE = true;
                        }
                break;
        }
    }

*/

    @Override
    public void onPause() {
        super.onPause();
        Log.e("TAG","Stop handler ");
        if(myRunnable != null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler.removeCallbacks(myRunnable);
        }
    }
}
