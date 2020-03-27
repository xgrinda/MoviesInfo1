package com.mzapps.app.cotoflix.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mzapps.app.cotoflix.Activitys.ActorActivity;
import com.mzapps.app.cotoflix.MainActivity;
import com.mzapps.app.cotoflix.Model.Actor;
import com.mzapps.app.cotoflix.R;

import java.util.List;

/**
 * Created by XgRiNdA on 05,August,2019
 */
public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.CastViewHolder>{




    private List<Actor> actors;
    private Context mContext;
    private int counter =1;


    public static class CastViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView thumbnail;
        public CastViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            thumbnail = (ImageView) v.findViewById(R.id.thumbnail);

        }
    }

    public ActorAdapter(List<Actor> actors, Context mContext) {
        this.actors = actors;
        this.mContext = mContext;
    }

    @Override
    public ActorAdapter.CastViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_actor, parent, false);
        return new ActorAdapter.CastViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ActorAdapter.CastViewHolder holder, final int position) {

        holder.name.setText(actors.get(position).getName());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.if_person);
        requestOptions.error(R.drawable.if_person);
        Glide.with(mContext).setDefaultRequestOptions(requestOptions).load("http://image.tmdb.org/t/p/w185"+actors.get(position)
                .getProfilePath()).apply(RequestOptions
                .circleCropTransform())
                .into(holder.thumbnail);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActorActivity.class);
                intent.putExtra("person_id",actors.get(position).getId());
                mContext.startActivity(intent);
                ShowInterstitials();
            }
        });
    }

    @Override
    public int getItemCount() {
        return actors.size();
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

