package com.hacker.project.hackernewsproj.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hacker.project.hackernewsproj.R;
import com.hacker.project.hackernewsproj.adapter.CardNewsAdapter;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter<?> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private LinearLayout hackerNewsLayout;
    private LinearLayout newsLayout;
    private LinearLayout showLayout;
    private LinearLayout askLayout;
    private LinearLayout jobsLayout;
    private LinearLayout submitLayout;
    private LinearLayout logOutLayout;

    private DrawerLayout mDrawerLayout;
    private View mDrawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpLayout();

        mAdapter = new CardNewsAdapter(this, null);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void closeOrOpenDrawer(boolean drawerStatus) {
        if (drawerStatus) {
            mDrawerLayout.closeDrawer(mDrawer);
        } else {
            mDrawerLayout.openDrawer(mDrawer);
        }
    }


    @Override
    public void onClick(View v) {

        Intent intent = null/* = new Intent(MainActivity.this, MainActivity.class)*/;

        switch (v.getId()) {
            case R.id.ll_hacker_news:
                intent.putExtra(DATA_TYPE, HACKER_NEWS);
                break;
            case R.id.ll_news:
                intent.putExtra(DATA_TYPE, NEWS);
                break;
            case R.id.ll_ask:
                intent.putExtra(DATA_TYPE, ASK);
                break;
            case R.id.ll_job:
                intent.putExtra(DATA_TYPE, JOBS);
                break;
            case R.id.ll_show:
                intent.putExtra(DATA_TYPE, SHOW);
                break;
            case R.id.ll_submit:
//                intent.putExtra(DATA_TYPE, SUBMI);
                break;
            case R.id.ll_logout:
//                intent.putExtra(DATA_TYPE, LOG);
                break;
            case R.id.comment_holder:
            case R.id.url_holder:
            case R.id.news_title_tv:
                intent = new Intent(MainActivity.this, FullNewsActivity.class);
                break;

        }
        startActivity(intent);
        closeDrawer();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawer)) {
            mDrawerLayout.closeDrawer(mDrawer);
        } else {
            super.onBackPressed();
        }
    }

    private void closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(mDrawer))
            mDrawerLayout.closeDrawer(mDrawer);
    }

    private void setUpLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer = findViewById(R.id.navigation_drawer);
        hackerNewsLayout = (LinearLayout) findViewById(R.id.ll_hacker_news);
        newsLayout = (LinearLayout) findViewById(R.id.ll_news);
        showLayout = (LinearLayout) findViewById(R.id.ll_show);
        askLayout = (LinearLayout) findViewById(R.id.ll_ask);
        jobsLayout = (LinearLayout) findViewById(R.id.ll_job);
        submitLayout = (LinearLayout) findViewById(R.id.ll_submit);
        logOutLayout = (LinearLayout) findViewById(R.id.ll_logout);
        mRecyclerView = (RecyclerView) findViewById(R.id.news_list);
        mDrawer = (View) findViewById(R.id.navigation_drawer);

        hackerNewsLayout.setOnClickListener(this);
        newsLayout.setOnClickListener(this);
        showLayout.setOnClickListener(this);
        askLayout.setOnClickListener(this);
        jobsLayout.setOnClickListener(this);
        submitLayout.setOnClickListener(this);
        logOutLayout.setOnClickListener(this);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mDrawerLayout.setFocusableInTouchMode(false);
//        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mDrawerLayout.closeDrawer(mDrawer);
        mDrawerLayout.addDrawerListener(drawerListener);
    }

    DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View view, float v) {
            closeOrOpenDrawer(mDrawerLayout.isDrawerOpen(mDrawer));
        }

        @Override
        public void onDrawerOpened(View view) {
        }

        @Override
        public void onDrawerClosed(View view) {
        }

        @Override
        public void onDrawerStateChanged(int i) {
        }
    };
}
