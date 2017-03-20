package com.pjm284.nytimessearch.models;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.pjm284.nytimessearch.network.NYTimesSearchAPI;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class Search {
    String searchString;
    Calendar startDate;
    String sortOrder;
    boolean newsArts;
    boolean newsFashion;
    boolean newsSports;
    int pageNumber;

    public Search() {
        this.pageNumber = 0;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public String getSortOrder() { return sortOrder; }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isNewsArts() {
        return newsArts;
    }

    public void setNewsArts(boolean newsArts) {
        this.newsArts = newsArts;
    }

    public boolean isNewsFashion() {
        return newsFashion;
    }

    public void setNewsFashion(boolean newsFashion) {
        this.newsFashion = newsFashion;
    }

    public boolean isNewsSports() {
        return newsSports;
    }

    public void setNewsSports(boolean newsSports) {
        this.newsSports = newsSports;
    }

    public int getPageNumber() { return pageNumber; }

    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }

    public void getResults(JsonHttpResponseHandler searchResultHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        NYTimesSearchAPI api = new NYTimesSearchAPI(client);

        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("q", this.getSearchString().trim());

        if (getStartDate() != null) {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
            String dateFormatted = format1.format(getStartDate().getTime());

            queryParams.put("begin_date", dateFormatted);
        }

            queryParams.put("page", Integer.toString(getPageNumber()));

        if (getSortOrder() != null) {
            queryParams.put("sort", getSortOrder());
        }

        String newsDeskParam = "";

        if (isNewsArts()) {
            newsDeskParam += "\"Arts\" ";
        }
        if (isNewsFashion()) {
            newsDeskParam += "\"Fashion & Style\" ";
        }
        if (isNewsSports()) {
            newsDeskParam += "\"Sports\" ";
        }

        if (!newsDeskParam.equals("")) {
            queryParams.put("fq", "news_desk:(" + newsDeskParam + ")");
        }



        api.search(queryParams, searchResultHandler);
    }
}
