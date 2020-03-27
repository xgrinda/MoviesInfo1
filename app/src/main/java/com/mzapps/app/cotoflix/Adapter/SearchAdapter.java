package com.mzapps.app.cotoflix.Adapter;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzapps.app.cotoflix.Activitys.DetailsMovie_Activity;
import com.mzapps.app.cotoflix.Activitys.DetailsTV_Activity;
import com.mzapps.app.cotoflix.Model.Movie;
import com.mzapps.app.cotoflix.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<Movie> movies;
    private Context mContext;


    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView Title;
        TextView year,overview;
        ImageView thumbnail,tv;
        public SearchViewHolder(View v) {
            super(v);
            Title = (TextView) v.findViewById(R.id.Title);
            year = (TextView) v.findViewById(R.id.year);
            overview = (TextView) v.findViewById(R.id.overview);
            thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
            tv = (ImageView) v.findViewById(R.id.tv);

        }
    }

    public SearchAdapter(List<Movie> movies, Context mContext) {
        this.movies = movies;
        this.mContext = mContext;
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SearchAdapter.SearchViewHolder holder, final int position) {

        final Movie movie = movies.get(position);


        if (movie.getTitle() == null) {
            holder.Title.setText(movie.getOriginal_name());
        } else {
            holder.Title.setText(movie.getOriginalTitle());
        }
        if (movie.getReleaseDate() != null) {
            holder.year.setText(movie.getReleaseDate());
        } else {
            holder.year.setText(movie.getFirst_air_date());
        }
        if (movie.getMedia_type().equals("tv")) {
            holder.tv.setVisibility(View.VISIBLE);
        }


        holder.overview.setText(movie.getOverview());

        Glide.with(mContext).load("http://image.tmdb.org/t/p/w500" + movies.get(position).getPosterPath()).into(holder.thumbnail);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(movie.getTitle() == null){

                    Activity(movie,mContext, DetailsTV_Activity.TAG_DetailsTV_Fragment);

                }else {

                    Activity(movie,mContext, DetailsMovie_Activity.TAG_DetailsMovie_Fragment);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    private void Activity(Movie movie,Context mContext,String TAG) {
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
}
