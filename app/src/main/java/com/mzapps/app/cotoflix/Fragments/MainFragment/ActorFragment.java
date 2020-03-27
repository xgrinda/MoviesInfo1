package com.mzapps.app.cotoflix.Fragments.MainFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mzapps.app.cotoflix.Adapter.ActorAdapter;
import com.mzapps.app.cotoflix.BuildConfig;
import com.mzapps.app.cotoflix.Model.Actor;
import com.mzapps.app.cotoflix.Model.ActorResponse;
import com.mzapps.app.cotoflix.R;
import com.mzapps.app.cotoflix.Utility.ApiClient;
import com.mzapps.app.cotoflix.Utility.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by XgRiNdA on 04,August,2019
 */
public class ActorFragment extends Fragment {

    Context mContext;
    Handler mHandler;
    Runnable myRunnable;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager mLayoutManager;
    private boolean userScrolled = true;
    RecyclerView recyclerView_actors;
    private RelativeLayout bottomLayout;
    private List<Actor> actors = new ArrayList<Actor>();
    private List<Actor> actors1 = new ArrayList<Actor>();
    private static int  pageIndex ;
    ApiInterface apiService;
    String TMBDB_API_KEY = BuildConfig.TMBDB_API_KEY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = this.getContext();
        View view = inflater.inflate(R.layout.actors_fragment, container, false);
        pageIndex = 1;

        recyclerView_actors = (RecyclerView)view.findViewById(R.id.recyclerView_actors1);
        bottomLayout = view.findViewById(R.id.loadItemsLayout_recyclerView);
        mLayoutManager = new GridLayoutManager(mContext,3);
        recyclerView_actors.setLayoutManager(mLayoutManager);
        retrofit();
        Pagination();
        return view;

    }


    private void retrofit() {

        String BASE_URL = "https://api.themoviedb.org/3/";
        apiService = ApiClient.getClient(mContext, BASE_URL).create(ApiInterface.class);

        Call<ActorResponse> call_recyclerView_actors = apiService.getpopularActors(TMBDB_API_KEY,pageIndex);
        call_recyclerView_actors.enqueue(new Callback<ActorResponse>() {
            @Override
            public void onResponse(Call<ActorResponse> call, Response<ActorResponse> response) {
                pageIndex++;
                try {

                    if (pageIndex>=2){
                        actors1 = response.body().getResults();
                        actors.addAll(actors1);
                        recyclerView_actors.setAdapter(new ActorAdapter(actors, mContext));
                        recyclerView_actors.scrollToPosition(pastVisiblesItems);
                    }else {
                        actors = response.body().getResults();
                        recyclerView_actors.setAdapter(new ActorAdapter(actors,mContext));
                    }

                }catch (Exception e ){
                    Log.e("Exception::",e.toString());
                }

            }
            @Override
            public void onFailure(Call<ActorResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("TAG", "Stop handler ");
        if (myRunnable != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.removeCallbacks(myRunnable);
        }
    }

    private void Pagination() {
        recyclerView_actors.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {

                super.onScrollStateChanged(recyclerView, newState);

                // If scroll state is touch scroll then set userScrolled
                // true
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;

                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,
                                   int dy) {

                super.onScrolled(recyclerView, dx, dy);
                // Here get the child count, item count and visibleitems
                // from layout manager

                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager
                        .findFirstVisibleItemPosition();

                // Now check if userScrolled is true and also check if
                // the item is end then update recycler view and set
                // userScrolled to false
                if (userScrolled
                        && (visibleItemCount + pastVisiblesItems) == totalItemCount) {
                    userScrolled = false;
                    updateRecyclerView();
                }
            }

        });
    }

    private void updateRecyclerView() {
        bottomLayout.setVisibility(View.VISIBLE);
        mHandler=  new Handler();
        myRunnable = new Runnable() {
            public void run() {
                retrofit();

                bottomLayout.setVisibility(View.GONE);
            }
        };
        mHandler.postDelayed(myRunnable,2000);


    }

}

