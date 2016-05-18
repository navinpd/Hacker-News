package com.hacker.project.hackernewsproj.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.hacker.project.hackernewsproj.R;
import com.hacker.project.hackernewsproj.adapter.CardNewsAdapter;
import com.hacker.project.hackernewsproj.data.Constants;
import com.hacker.project.hackernewsproj.data.JsonData;

import org.androidannotations.annotations.EActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
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

    private Firebase preURL;
    private Firebase mainURL;
    private ArrayList<Long> submissionIDs;
    int loadedSubmissions = 0;
    int submissionUpdateNum = 15;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpLayout();

        preURL = new Firebase(Constants.BASE_URL);
        mainURL = preURL.child("/topstories");

        mAdapter = new CardNewsAdapter(this, null);
        mRecyclerView.setAdapter(mAdapter);

        updateSubmissions();

    }

    private void closeOrOpenDrawer(boolean drawerStatus) {
        if (drawerStatus) {
            mDrawerLayout.closeDrawer(mDrawer);
        } else {
            mDrawerLayout.openDrawer(mDrawer);
        }
    }

    public void updateSubmissions() {
        if (submissionIDs == null) {
            progressDialog.show();

            mainURL.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    submissionIDs = (ArrayList<Long>) snapshot.getValue();

                    // Because we are doing this asynchronously, it's easier to update submissions directly
                    updateSubmissions();

                    // Hide the progress bar
                    progressDialog.dismiss();
//                    footer.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.err.println("Could not retrieve posts! " + firebaseError);
//                    no_submissions.setVisibility(View.VISIBLE);
//                    progress.setVisibility(View.GONE);
//                    footer.setVisibility(View.GONE);
                }
            });
        } else {

            // We cannot use feedAdapter.getCount() directly since that may lead to race conditions
            int start = loadedSubmissions;

            // From the top 500 submissions, we only load a few at a time
            for (; loadedSubmissions < start + submissionUpdateNum && loadedSubmissions < submissionIDs.size(); loadedSubmissions++) {
                // But we must first add each submission to the view manually
                updateSingleSubmission(submissionIDs.get(loadedSubmissions));
            }

//            if (loadedSubmissions == submissionIDs.size()) {
//                no_submissions.setVisibility(View.VISIBLE);
//            }
        }
    }

    // Gets an url to a single submission and updates it in the feedadapter
    public void updateSingleSubmission(final Long submissionId) {

        Firebase submission = new Firebase("https://hacker-news.firebaseio.com/v0/item/" + submissionId);

        submission.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                // We retrieve all objects into a hashmap
                Map<String, Object> ret = (Map<String, Object>) snapshot.getValue();

                if (ret == null) {
                    return;
                }

                String url = (String) ret.get("url");
                URL site = null;

                // If the url exists
                if (url != null) {
                    try {
                        site = new URL(url);
                    } catch (MalformedURLException e) {
                        System.err.println("Malformed url: " + url);
                    }
                }

                JsonData f = initNewFeedItem(submissionId, ret, site);
                ((CardNewsAdapter) mAdapter).addData(f);
                mAdapter.notifyDataSetChanged();

//                if (site != null) {
//                    // Asynchronously updates images for the feed item
//                    updateSubmissionThumbnail(site.getHost(), f);
//                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.err.println("Could not retrieve post! " + firebaseError);
            }
        });
    }

    // Takes the raw API data and the URL, returns a new feed item
    public JsonData initNewFeedItem(Long submissionId, Map<String, Object> ret, URL site) {

        JsonData f = new JsonData();

        f.setId(submissionId);

        // Gets readable date
        String time = updateDate((Long) ret.get("time"));

        // Set titles and other data
        f.setTitle((String) ret.get("title"));
        f.setText((String) ret.get("text"));
        f.setBy((String) ret.get("by"));
        f.setScore((Long) ret.get("score"));
        f.setTime(time);

        // Jobs stories don't have any descendants, we need to take care of that
        Object descendantObject = ret.get("descendants");
        if (descendantObject != null) {
            f.setDescendants((Long) descendantObject);
        } else {
            System.err.println("Null descendants: " + ret.get("title"));
            f.setDescendants(0L);
        }

        // Hacker News site urls are null
        if (site != null) {
            String domain = site.getHost().replace("www.", "");
//            f.setShortUrl(domain);
            f.setUrl(site.toString());
//            f.setLetter(domain.substring(0, 1));
//        } else {
//            // The hacker news submissions don't technically have an url, so we cheat
//            f.setShortUrl("Hacker News");
//            f.setLetter("HN");
        }

        // Generate TextDrawable thumbnail
//        TextDrawable.IShapeBuilder builder = TextDrawable.builder().beginConfig().bold().toUpperCase().endConfig();
//        TextDrawable drawable = builder.buildRect(f.getLetter(), f.getColor());
//        f.setTextDrawable(drawable);

        return f;
    }

    // Converts the difference between two dates into a date
    public String updateDate(Long time) {

        Date past = new Date(time * 1000);
        Date now = new Date();

        if (TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) > 0) {
            return TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + "d";
        } else if (TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) > 0) {
            return TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + "h";
        }
        if (TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) > 0) {
            return TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + "m";
        } else {
            return TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) + "s";
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
                int position = (int) v.getTag();
                intent = new Intent(MainActivity.this, FullNewsActivity.class);
                intent.putExtra("URL", ((CardNewsAdapter) mAdapter).getURL(position));
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
        progressDialog = new ProgressDialog(MainActivity.this);

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
        Firebase.setAndroidContext(getApplicationContext());

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
