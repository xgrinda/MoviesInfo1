package com.mzapps.app.cotoflix.Activitys;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.mzapps.app.cotoflix.Adapter.SearchAdapter;
import com.mzapps.app.cotoflix.BuildConfig;
import com.mzapps.app.cotoflix.Model.Movie;
import com.mzapps.app.cotoflix.Model.MoviesResponse;
import com.mzapps.app.cotoflix.R;
import com.mzapps.app.cotoflix.Utility.ApiClient;
import com.mzapps.app.cotoflix.Utility.ApiInterface;
import com.mzapps.app.cotoflix.Utility.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    Context mContext;
    RecyclerView recyclerView_search;
    LinearLayout layout_search,layout_offline_searchfragment;
    EditText edit_query;
    ImageView clear_search;
    String  TMBDB_API_KEY = BuildConfig.TMBDB_API_KEY;
    String BASE_URL = "https://api.themoviedb.org/3/";
    LottieAnimationView animation_search_fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_activity);
        recyclerView_search = findViewById(R.id.recyclerView_search);
        layout_search = findViewById(R.id.layout_search);
        layout_offline_searchfragment = findViewById(R.id.layout_offline_searchfragment);
        clear_search = findViewById(R.id.clear_search);
        animation_search_fragment = findViewById(R.id.animation_search_fragment);

        mContext = this;


        edit_query = findViewById(R.id.edit_query);
        edit_query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                recyclerView_search.setVisibility(View.VISIBLE);
                layout_search.setVisibility(View.GONE);
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!new Utils().isNetworkAvailable(mContext)){
                    layout_offline_searchfragment.setVisibility(View.VISIBLE);
                    animation_search_fragment.setAnimation(R.raw.no_internet);
                    animation_search_fragment.playAnimation();
                }else {
                    layout_offline_searchfragment.setVisibility(View.GONE);
                    getDate(charSequence);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String ed_text = edit_query.getText().toString().trim();
                if(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null)
                {
                    //EditText is empty
                    recyclerView_search.setVisibility(View.GONE);
                    layout_search.setVisibility(View.VISIBLE);
                    clear_search.setVisibility(View.GONE);
                }
                else
                {
                    //EditText is not empty
                    clear_search.setVisibility(View.VISIBLE);
                }
            }
        });
        edit_query.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    edit_query.setHint("");
                    clear_search.setVisibility(View.VISIBLE);
            }
        });
        clear_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_query.setText("");
            }
        });
        hide_keyboard(this);

    }



    private void getDate(CharSequence Char) {
        String query = Char.toString();

        ApiInterface apiService =
                ApiClient.getClient(mContext,BASE_URL).create(ApiInterface.class);


        //setRecyclerView_search
        //==================================================================
        Call<MoviesResponse> call_RecyclerView_search = apiService.getMovieSearch(query,TMBDB_API_KEY);
        call_RecyclerView_search.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> movies = null;
                List<Movie> movies2 = new ArrayList<>();

                try {
                    if (response.body().getResults() != null )
                    {   movies = response.body().getResults();
                        for(Movie s:movies){
                            if (!s.getMedia_type().equals("person")){
                                movies2.add(s);
                            }
                        }
                        recyclerView_search.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                        recyclerView_search.setAdapter(new SearchAdapter(movies2, mContext));
                    }
                }catch (Exception e ){
                    Log.e("Exception::",e.toString());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
            }
        });
    }
    private void hide_keyboard(final AppCompatActivity activity) {
        recyclerView_search.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = activity.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }
}