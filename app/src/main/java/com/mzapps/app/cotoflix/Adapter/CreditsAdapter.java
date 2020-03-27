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
import com.bumptech.glide.request.RequestOptions;
import com.mzapps.app.cotoflix.Activitys.ActorActivity;
import com.mzapps.app.cotoflix.Model.Cast;
import com.mzapps.app.cotoflix.R;

import java.util.List;


public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.CreditsViewHolder> {

    private List<Cast> casts;
    private Context mContext;


    public static class CreditsViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView character;
        ImageView thumbnail;
        public CreditsViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            character = (TextView) v.findViewById(R.id.character);
            thumbnail = (ImageView) v.findViewById(R.id.thumbnail);

        }
    }

    public CreditsAdapter(List<Cast> casts, Context mContext) {
        this.casts = casts;
        this.mContext = mContext;
    }

    @Override
    public CreditsAdapter.CreditsViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cast, parent, false);
        return new CreditsAdapter.CreditsViewHolder(view);
    }
    @Override
    public void onBindViewHolder(CreditsAdapter.CreditsViewHolder holder, final int position) {

        holder.name.setText(casts.get(position).getName());
        holder.character.setText(casts.get(position).getCharacter());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.if_person);
        requestOptions.error(R.drawable.if_person);
        Glide.with(mContext).setDefaultRequestOptions(requestOptions).load("http://image.tmdb.org/t/p/w185"+casts.get(position).getProfilePath()).apply(RequestOptions.circleCropTransform()).into(holder.thumbnail);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActorActivity.class);
                intent.putExtra("person_id",casts.get(position).getId());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

}
