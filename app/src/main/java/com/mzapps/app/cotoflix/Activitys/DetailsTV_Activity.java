package com.mzapps.app.cotoflix.Activitys;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mzapps.app.cotoflix.Adapter.CreditsAdapter;
import com.mzapps.app.cotoflix.Adapter.CrewAdapter;
import com.mzapps.app.cotoflix.Adapter.EpisodesAdapter;
import com.mzapps.app.cotoflix.Adapter.ImagesAdapter;
import com.mzapps.app.cotoflix.Adapter.TrailerAdapter;
import com.mzapps.app.cotoflix.BuildConfig;
import com.mzapps.app.cotoflix.Model.Backdrop;
import com.mzapps.app.cotoflix.Model.Cast;
import com.mzapps.app.cotoflix.Model.Credits;
import com.mzapps.app.cotoflix.Model.Crew;
import com.mzapps.app.cotoflix.Model.Episode;
import com.mzapps.app.cotoflix.Model.Genre;
import com.mzapps.app.cotoflix.Model.Images_tmdb;
import com.mzapps.app.cotoflix.Model.Link;
import com.mzapps.app.cotoflix.Model.Movie;
import com.mzapps.app.cotoflix.Model.SeasonDetails;
import com.mzapps.app.cotoflix.Model.TVDetails;
import com.mzapps.app.cotoflix.Model.Trailer;
import com.mzapps.app.cotoflix.Model.TrailerResponse;
import com.mzapps.app.cotoflix.R;
import com.mzapps.app.cotoflix.Utility.ApiClient;
import com.mzapps.app.cotoflix.Utility.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class DetailsTV_Activity extends AppCompatActivity {

    private static boolean LIKE = true;
    private AdView mAdView;
    public static final String TAG_DetailsTV_Fragment = "TAG_DetailsTV_Fragment";

    TextView title,year,rating,overview,runtime,voteCount,genre1,genre2,genre3,number_of_seasons,number_of_episodes,id_number;
    ImageView cover;
    Context mContext;
    RecyclerView recyclerView_trailers,recyclerView_actors,recyclerView_crew,recyclerView_images,recyclerView_episodes;
    RelativeLayout layout_genre1,layout_genre2,layout_genre3;
    LinearLayout trailer_layout,actors_layout,crew_layout,image_layout,episodes_layout;
    RatingBar ratingBar;
    ImageView like,watch;
    ProgressBar progressBar;
    LinearLayout layout;
    Spinner spinner;
    int x;
    String TMBDB_API_KEY= BuildConfig.TMBDB_API_KEY;
    Movie movie;
    Handler mHandler;
    Runnable myRunnable;
    Link l = new Link();
    String BASE_URL = "https://api.themoviedb.org/3/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.details_tv_activity_layout);
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
        id_number = findViewById(R.id.id_number);

        number_of_episodes = findViewById(R.id.number_of_episodes);
        number_of_seasons = findViewById(R.id.number_of_seasons);

        genre1 = findViewById(R.id.genre1);
        genre2 = findViewById(R.id.genre2);
        genre3 = findViewById(R.id.genre3);
        layout_genre1 = findViewById(R.id.layout_genre1);
        layout_genre2 = findViewById(R.id.layout_genre2);
        layout_genre3 = findViewById(R.id.layout_genre3);
        like = findViewById(R.id.like);
        watch = findViewById(R.id.watch);
        trailer_layout = findViewById(R.id.trailer_layout);
        actors_layout = findViewById(R.id.actors_layout);
        image_layout = findViewById(R.id.image_layout);
        episodes_layout = findViewById(R.id.episodes_layout);

        recyclerView_trailers = findViewById(R.id.recyclerView_trailers);
        recyclerView_actors = findViewById(R.id.recyclerView_actors);
        recyclerView_crew = findViewById(R.id.recyclerView_crew);
        recyclerView_images = findViewById(R.id.recyclerView_images);
        recyclerView_episodes = findViewById(R.id.recyclerView_episodes);

        crew_layout = findViewById(R.id.crew_layout);

        spinner= findViewById(R.id.spinner_episodes);


        progressBar =  findViewById(R.id.progressBar_tv);
        layout =  findViewById(R.id.layout_tv);

        movie = (Movie)getIntent().getExtras().getSerializable("movie");
        x= movie.getId();


        retrofit();
        Like();
    }

    private void retrofit() {

        progressBar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);

        final ApiInterface apiService = ApiClient.getClient(mContext,BASE_URL).create(ApiInterface.class);

        Call<TVDetails> call_TV_details = apiService.getTVDetails(x,TMBDB_API_KEY);
        call_TV_details.enqueue(new Callback<TVDetails>() {
            @Override
            public void onResponse(Call<TVDetails> call, Response<TVDetails> response) {
                TVDetails m = response.body();


                    try{
                        title.setText(m.getOriginalName());
                        year.setText(m.getLastAirDate().substring(0,4));
                        rating.setText(m.getVoteAverage().toString());
                        overview.setText(m.getOverview());
                        ratingBar.setRating(m.getVoteAverage().floatValue()/2);
                        voteCount.setText(m.getVoteCount().toString());
                        id_number.setText("ID:" + String.valueOf(x));

                        number_of_episodes.setText("Number of episodes: "+m.getNumberOfEpisodes().toString());
                        number_of_seasons.setText("Number of seasons: "+m.getNumberOfSeasons().toString());

                        l.setTitle(m.getOriginalName());
                        l.setYear(m.getLastAirDate().substring(0,4));

                        setgenre(m.getGenres(),m.getGenres().size());
                        String cover_string_link ;
                        if(movie.getBackdropPath() == null){
                            cover_string_link = "http://image.tmdb.org/t/p/w780"+m.getPosterPath().toString();
                        }else {
                            cover_string_link = "http://image.tmdb.org/t/p/w780"+m.getBackdropPath().toString();
                        }
                        Glide.with(mContext)
                                .load(cover_string_link)
                                .transition(withCrossFade())
                                .into(cover);
                        List<String> seasons = new ArrayList<>();
                        for(int i=0;i< m.getNumberOfSeasons();i++){
                            seasons.add(i,"SEASON "+(i+1));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.spinner_item_layout, seasons);
                        spinner.setAdapter(adapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                // your code here
                                episodes(position+1);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                            }

                        });

                        progressBar.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);

                    }catch (Exception e){
                        Log.i("TAG :",e.toString());
                    }

            }

            @Override
            public void onFailure(Call<TVDetails> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG :", t.toString());
            }
        });


        //setRecyclerView_actors
        //==================================================================
        Call<Credits> call_recyclerView_actors = apiService.getTVCredits(x,TMBDB_API_KEY);
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
        //call_recyclerView_crew
        //==================================================================
        Call<Credits> call_recyclerView_crew = apiService.getTVCredits(x,TMBDB_API_KEY);
        call_recyclerView_crew.enqueue(new Callback<Credits>() {
            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {
                List<Crew> crews = null;
                try {
                    crews = response.body().getCrew();
                    crew_layout.setVisibility(View.VISIBLE);
                    recyclerView_crew.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                    recyclerView_crew.setAdapter(new CrewAdapter(crews, mContext));
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

        //setRecyclerView_images
        //==================================================================
        Call<Images_tmdb> call_RecyclerView_images = apiService.getImages("tv",x,TMBDB_API_KEY);
        call_RecyclerView_images.enqueue(new Callback<Images_tmdb>() {
            @Override
            public void onResponse(Call<Images_tmdb> call, Response<Images_tmdb> response) {
                List<Backdrop> posters;
                //List<Poster> posters;
                try {
                    posters = response.body().getBackdrops();
                    image_layout.setVisibility(View.VISIBLE);
                    recyclerView_images.setLayoutManager(new LinearLayoutManager(mContext,
                            LinearLayoutManager.HORIZONTAL, false));
                    recyclerView_images.setAdapter(new ImagesAdapter(posters, mContext));
                    if (posters.size() > 0) {
                        change_backdrop(posters);
                    }
                }catch (Exception e){   }
            }

            @Override
            public void onFailure(Call<Images_tmdb> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });
        //setRecyclerView_trailers
        //==================================================================
        Call<TrailerResponse> call_RecyclerView_trailers = apiService.gettrailers("tv",x,TMBDB_API_KEY);
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
                Log.e("tag", t.toString());
            }
        });

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

    private void Like() {
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LIKE){
                    like.setImageResource(R.drawable.if_heart_119_111093);
                    LIKE = false;
                }else {
                    like.setImageResource(R.drawable.if_heart_1814104);
                    LIKE = true;
                }
            }
        });

    }

    private void episodes(final int Season_number) {

        final ApiInterface apiService =
                ApiClient.getClient(mContext,BASE_URL).create(ApiInterface.class);
        Call<SeasonDetails> call_RecyclerView_episodes = apiService.getepisodes(x,Season_number,TMBDB_API_KEY);
                                   call_RecyclerView_episodes.enqueue(new Callback<SeasonDetails>() {
                                       @Override
                                       public void onResponse(Call<SeasonDetails> call, Response<SeasonDetails> response) {
                                           List<Episode> episodes;

                                           episodes = response.body().getEpisodes();
                                           episodes_layout.setVisibility(View.VISIBLE);
                                           recyclerView_episodes.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
                                           l.setId_tvseries_tmdb(x);
                                           l.setSeason_number(Season_number);
                                           l.setName_tv_series(movie.getOriginal_name());

                                           recyclerView_episodes.setAdapter(new EpisodesAdapter(episodes,l, mContext));
                                           recyclerView_episodes.setNestedScrollingEnabled(false);


                                       }

                                       @Override
                                       public void onFailure(Call<SeasonDetails> call, Throwable t) {
                                           Log.e("tag", t.toString());
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



    @Override
    public void onPause() {
        super.onPause();
        Log.e("TAG","Stop handler ");
        if(myRunnable != null){
            mHandler.removeCallbacksAndMessages(null);
        }


    }

}
