package com.mzapps.app.cotoflix.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by XgRiNdA on 05,August,2019
 */
public class ActorResponse {
    @SerializedName("results")
    private List<Actor> Actor = null;


    @SerializedName("page")
    private Integer page;

    @SerializedName("total_results")
    private Integer totalResults;

    @SerializedName("total_pages")
    private Integer totalPages;

    public List<Actor> getResults() {
        return Actor;
    }

    public void setResults(List<Actor> results) {
        this.Actor = results;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
