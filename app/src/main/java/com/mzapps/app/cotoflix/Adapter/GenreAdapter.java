package com.mzapps.app.cotoflix.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mzapps.app.cotoflix.Model.Genre;
import com.mzapps.app.cotoflix.R;

import java.util.List;

/**
 * Created by XgRiNdA on 05,August,2019
 */
public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    private List<Genre> genres;
    private Context mContext;


    public static class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public GenreViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);

        }
    }

    public GenreAdapter(List<Genre> genres, Context mContext) {
        this.genres = genres;
        this.mContext = mContext;
    }

    @Override
    public GenreAdapter.GenreViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_genre, parent, false);
        return new GenreAdapter.GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GenreAdapter.GenreViewHolder holder, final int position) {

        holder.name.setText(genres.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                Intent intent = new Intent(mContext, MoreActivity.class);
                intent.putExtra("genre_id",genres.get(position).getId());
                mContext.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

}
