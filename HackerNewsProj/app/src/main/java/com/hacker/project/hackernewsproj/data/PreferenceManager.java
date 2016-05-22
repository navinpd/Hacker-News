package com.hacker.project.hackernewsproj.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Navin on 21/05/16.
 */
public class PreferenceManager {
    private static PreferenceManager manager;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static final String MY_PREFS = "MY_PREFS";
    private static final String HACKER_STORIES = "HACKER_STORIES";
    private static final String HACKER_JOBS = "HACKER_JOBS";
    private static final String HACKER_ASK = "HACKER_ASK";
    private static final String HACKER_SHOW = "HACKER_SHOW";
    private static final String HACKER_NEWS = "HACKER_NEWS";


    public static PreferenceManager getManager(Context context) {
        if (manager == null) {
            manager = new PreferenceManager();
            sharedPreferences = context.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }

        return manager;
    }

    public void shareHackerNews(String value) {
        editor.putString(HACKER_STORIES, value);
        editor.commit();
    }

    public String getHackerNews() {
        return sharedPreferences.getString(HACKER_STORIES, "");
    }

    public void shareJobs(String value) {
        editor.putString(HACKER_JOBS, value);
        editor.commit();
    }

    public String getJobs() {
        return sharedPreferences.getString(HACKER_JOBS, "");
    }

    public void shareAsk(String value) {

        editor.putString(HACKER_ASK, value);
        editor.commit();
    }

    public String getAsk() {
        return sharedPreferences.getString(HACKER_ASK, "");
    }

    public void shareShow(String value) {

        editor.putString(HACKER_SHOW, value);
        editor.commit();
    }

    public String getShow() {
        return sharedPreferences.getString(HACKER_SHOW, "");
    }

    public void shareNews(String value) {

        editor.putString(HACKER_NEWS, value);
        editor.commit();
    }

    public String getNews() {
        return sharedPreferences.getString(HACKER_NEWS, "");
    }


}
