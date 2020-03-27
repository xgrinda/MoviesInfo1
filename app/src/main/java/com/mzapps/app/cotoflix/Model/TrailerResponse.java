package com.mzapps.app.cotoflix.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TrailerResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Trailer> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }

}
