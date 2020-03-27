package com.mzapps.app.cotoflix.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mzapps.app.cotoflix.Fragments.MainFragment.ActorFragment;
import com.mzapps.app.cotoflix.Fragments.MainFragment.GenresFragment;
import com.mzapps.app.cotoflix.Fragments.MainFragment.HomeFragment;
import com.mzapps.app.cotoflix.Fragments.MainFragment.TvShowsFragment;

/**
 * Created by XgRiNdA on 03,August,2019
 */
public class PagerAdapter extends FragmentPagerAdapter {

    int tabCount;

    //Constructor to the class
    public PagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                TvShowsFragment tvShowsFragment = new TvShowsFragment();
                return tvShowsFragment;
            case 2:
                ActorFragment castFragment = new ActorFragment();
                return castFragment;
            case 3:
                GenresFragment genresFragment = new GenresFragment();
                return genresFragment;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
