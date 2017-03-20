package com.pjm284.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Article implements Serializable {
    String webUrl;
    String headline;
    Photo photo;

    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            for(int i=0; i<multimedia.length(); i++){
                JSONObject multimediaJson = multimedia.getJSONObject(i);
                this.photo = new Photo("http://www.nytimes.com/" + multimediaJson.getString("url"), multimediaJson.getInt("width"), multimediaJson.getInt("height"));
                if (multimediaJson.getString("subtype").equals("xlarge")) {
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array) {
        ArrayList<Article> results = new ArrayList<>();

        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new Article(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }
}
