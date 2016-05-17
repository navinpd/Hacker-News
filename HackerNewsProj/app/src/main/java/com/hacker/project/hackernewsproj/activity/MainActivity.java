package com.hacker.project.hackernewsproj.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hacker.project.hackernewsproj.R;
import com.hacker.project.hackernewsproj.adapter.CardNewsAdapter;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements View.OnClickListener {

    @ViewById(R.id.tv_address)
    TextView addressText;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter<?> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    LinearLayout hackerNewsLayout;
    LinearLayout newsLayout;
    LinearLayout showLayout;
    LinearLayout askLayout;
    LinearLayout jobsLayout;
    LinearLayout submitLayout;
    LinearLayout logOutLayout;

    private DrawerLayout mDrawerLayout;

    @ViewById(R.id.navigation_drawer)
    private View mDrawer;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new CardNewsAdapter(this, null);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mRecyclerView.setAdapter(mAdapter);


        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mDrawerLayout.setFocusableInTouchMode(false);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mDrawerLayout.closeDrawer(mDrawer);
        mDrawerLayout.addDrawerListener(drawerListener);
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
        switch (v.getId()) {
            case R.id.ll_hacker_news:
                break;
            case R.id.ll_news:
                break;
            case R.id.ll_ask:
                break;
            case R.id.ll_job:
                break;
            case R.id.ll_menu_items:
                break;
            case R.id.ll_submit:
                break;
            case R.id.ll_logout:
                break;

        }
    }
}
