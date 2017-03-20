package com.pjm284.nytimessearch.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;


public class NYTimesSearchAPI {
    private static final String searchUrl = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    private static final String apikey = "ad705c256dcb4e81b1e5529e7e5d742d";

    private AsyncHttpClient client;

    public NYTimesSearchAPI(AsyncHttpClient client) {

        this.client = client;
    }

    public void search(HashMap<String, String> queryParams, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("api-key", apikey);
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            params.put(entry.getKey(), entry.getValue());
        }

        client.get(searchUrl, params, handler);
    }
}
