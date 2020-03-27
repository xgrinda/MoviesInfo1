package com.mzapps.app.cotoflix.Adapter;


import android.content.Context;
import android.os.Vibrator;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.mzapps.app.cotoflix.Model.Backdrop;
import com.mzapps.app.cotoflix.R;
import com.mzapps.app.cotoflix.Utility.DownloadImage;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.TrailerViewHolder> {

    private List<Backdrop> posters;

    private Context mContext;


    public static class TrailerViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        public TrailerViewHolder(View v) {
            super(v);
            thumbnail = (ImageView) v.findViewById(R.id.thumbnail);

        }
    }

    public ImagesAdapter(List<Backdrop> posters, Context mContext) {
        this.posters = posters;
        this.mContext = mContext;
    }

    @Override
    public ImagesAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_images, parent, false);
        return new ImagesAdapter.TrailerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ImagesAdapter.TrailerViewHolder holder, final int position) {

        String url = "http://image.tmdb.org/t/p/w500"+posters.get(position).getFilePath();
        Glide.with(mContext).load(url)
                .transition(withCrossFade())
                .into(holder.thumbnail);


        holder.thumbnail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Vibrator vibe = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(150);
                Toast.makeText(mContext, "downloading...", Toast.LENGTH_SHORT).show();
                new DownloadImage(mContext,posters.get(position).getFilePath());

                return true;
            }
        });
    }




    @Override
    public int getItemCount() {
        return posters.size();
    }
}
