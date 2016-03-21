package com.example.khoa.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Article implements Parcelable {

    String webUrl;
    String headline;
    String thumbNail;
    String beginDate;
    String sort;
    String cbArts;
    String cbFashionStyle;
    String cbSports;

    public String getWebUrl() { return webUrl; }
    public String getHeadline() { return headline; }
    public String getThumbNail() { return thumbNail; }
    public String getBeginDate() { return beginDate; }
    public String getSort() { return sort; }
    public String getCbArts() { return cbArts; }
    public String getCbFashionStyle() { return cbFashionStyle; }
    public String getCbSports() { return cbSports; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.webUrl);
        dest.writeString(this.headline);
        dest.writeString(this.thumbNail);
        dest.writeString(this.beginDate);
        dest.writeString(this.sort);
        dest.writeString(this.cbArts);
        dest.writeString(this.cbFashionStyle);
        dest.writeString(this.cbSports);
    }

    public Article() {}

    // Constructor that will get object from parcel
    protected Article(Parcel in) {
        this.webUrl = in.readString();
        this.headline = in.readString();
        this.thumbNail = in.readString();
        this.beginDate = in.readString();
        this.sort = in.readString();
        this.cbArts = in.readString();
        this.cbFashionStyle = in.readString();
        this.cbSports = in.readString();
    }

    // Interface CREATOR that generates instances of Parcelable class
    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }
        @Override
        public Article[] newArray(int size) { return new Article[size]; }
    };

    // Decode a JSONObject object into an Article object
    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedias = jsonObject.getJSONArray("multimedia");
            if (multimedias.length() > 0) {
                JSONObject multimedia = multimedias.getJSONObject(0);
                this.thumbNail = "http://www.nytimes.com/" + multimedia.getString("url");
            } else {
                this.thumbNail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Decode array of Article JSONobjects into Article model objects
    public static ArrayList<Article> fromJSONArray(JSONArray array) {
        // Process each result in JSONArray, decode and convert to Article object
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
