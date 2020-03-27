package com.mzapps.app.cotoflix.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mzapps.app.cotoflix.Model.Episode;
import com.mzapps.app.cotoflix.Model.Link;
import com.mzapps.app.cotoflix.R;

import java.util.List;


public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.SearchViewHolder> {

    private List<Episode> episodes;
    private Context mContext;
    private Link l;

    public  class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView Title;
        TextView year,overview;

        ImageView thumbnail,tv;
        ProgressBar progresssbar_watch_eposides;


        public SearchViewHolder(View v) {
            super(v);
            Title =  v.findViewById(R.id.Title);
            year =  v.findViewById(R.id.year);
            overview =  v.findViewById(R.id.overview);
            thumbnail =  v.findViewById(R.id.thumbnail);
            tv =  v.findViewById(R.id.tv);


            progresssbar_watch_eposides = v.findViewById(R.id.progresssbar_watch_eposides);



        }
    }

    public EpisodesAdapter(List<Episode> episodes,Link l,Context mContext) {
        this.episodes = episodes;
        this.mContext = mContext;
        this.l = l;

    }

    @Override
    public EpisodesAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search, parent, false);
        return new EpisodesAdapter.SearchViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final EpisodesAdapter.SearchViewHolder holder, final int position) {

        final Episode episode = episodes.get(position);
        holder.Title.setText(episode.getName());
        holder.year.setText(episode.getAirDate());
        holder.overview.setText(episode.getOverview());
        Glide.with(mContext).load("http://image.tmdb.org/t/p/w300" + episode.getStillPath()).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }
}
