package com.hacker.project.hackernewsproj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Navin Prasad <navin.prasad@fastacash.com>
 * @since 17/May/2016
 */


public class BaseActivity extends AppCompatActivity {
    public static final String DATA_TYPE = "DATA_TYPE";
    public static final String HACKER_NEWS = "topstories";
    public static final String NEWS = "newstories";
    public static final String SHOW = "showstories";
    public static final String ASK = "askstories";
    public static final String JOBS = "jobstories";
    public static final String COMMENTS = "comments";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
