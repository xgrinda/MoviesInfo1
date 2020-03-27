package com.mzapps.app.cotoflix.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by XgRiNdA on 05,August,2019
 */
public class GenreResponse {

    @SerializedName("genres")
    private List<Genre> Genre = null;

    public List<Genre> getResults() {
        return Genre;
    }


}
