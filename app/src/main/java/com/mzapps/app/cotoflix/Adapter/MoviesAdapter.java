package com.mzapps.app.cotoflix.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mzapps.app.cotoflix.Activitys.DetailsMovie_Activity;
import com.mzapps.app.cotoflix.Activitys.DetailsTV_Activity;
import com.mzapps.app.cotoflix.MainActivity;
import com.mzapps.app.cotoflix.Model.Movie;
import com.mzapps.app.cotoflix.R;
import com.mzapps.app.cotoflix.Utility.DownloadImage;

import java.util.List;

import me.samthompson.bubbleactions.BubbleActions;
import me.samthompson.bubbleactions.Callback;



public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private Context mContext;
    private int counter = 1;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitle;
        TextView rating;
        ImageView thumbnail;
        RatingBar ratingBar;
        ProgressBar vote_average_progressbar;
        public MovieViewHolder(View v) {
            super(v);
            movieTitle =  v.findViewById(R.id.title);
            rating =  v.findViewById(R.id.rating);
            thumbnail =  v.findViewById(R.id.thumbnail);
            ratingBar =  v.findViewById(R.id.ratingbar);
            vote_average_progressbar =  v.findViewById(R.id.vote_average_progressbar);

        }
    }

    public MoviesAdapter(List<Movie> movies, Context mContext) {
        this.movies = movies;
        this.mContext = mContext;

    }

    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {

        final Movie movie = movies.get(position);
        if(movie.getTitle()==null) {
            holder.movieTitle.setText(movie.getOriginal_name());
        }else {
            holder.movieTitle.setText(movie.getTitle());
        }
        holder.rating.setText(movie.getVoteAverage().toString());
        holder.ratingBar.setRating(movie.getVoteAverage().floatValue()/2);
        //=================================================
        holder.vote_average_progressbar.setProgress(movie.getVoteAverage().intValue()*10);
        Glide.with(mContext).load("http://image.tmdb.org/t/p/w342"+movies.get(position).getPosterPath()).into(holder.thumbnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(movie.getTitle() == null){

                    Activity(movie,mContext, DetailsTV_Activity.TAG_DetailsTV_Fragment);
                    ShowInterstitials();
                }else {

                    Activity(movie,mContext, DetailsMovie_Activity.TAG_DetailsMovie_Fragment);
                    ShowInterstitials();
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                BubbleActions.on(view)
                        .addAction("Star", R.drawable.if_heart_119_111093, new Callback() {
                            @Override
                            public void doAction() {
                                //new RequestMovie(movie.getId().toString(),movie.getTitle()+" : "+movie.getReleaseDate().substring(0, 4),mContext);
                                Toast.makeText(view.getContext(), "Favourite!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addAction("Share", R.drawable.if_share4_216719, new Callback() {
                            @Override
                            public void doAction() {
                               // share(movie.getPosterPath());
                                new DownloadImage(mContext,movie.getPosterPath()).shareX();
                                Toast.makeText(view.getContext(), "Wait for share!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addAction("Hide", R.drawable.if_icon_close_round_211651, new Callback() {
                            @Override
                            public void doAction() {
                                Toast.makeText(view.getContext(), "Hide pressed!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                return false;
            }
        });
    }
    @Override
    public int getItemCount() {
        return movies.size();
    }
    private void Activity(Movie movie,Context mContext, String TAG) {
        if (TAG.equals(DetailsTV_Activity.TAG_DetailsTV_Fragment)){

            Intent intent = new Intent(mContext, DetailsTV_Activity.class);
            intent.putExtra("movie", movie);
            mContext.startActivity(intent);


        }else if (TAG.equals(DetailsMovie_Activity.TAG_DetailsMovie_Fragment)){

            Intent intent = new Intent(mContext, DetailsMovie_Activity.class);
            intent.putExtra("movie", movie);
            mContext.startActivity(intent);


        }
    }
    private void ShowInterstitials(){

            if (counter == 2){
                if (MainActivity.mInterstitialAd.isLoaded()) {
                    MainActivity.mInterstitialAd.show();
                }
                counter = 1;
            }else {
            counter++;
        }

    }

}
