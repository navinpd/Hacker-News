package com.hacker.project.hackernewsproj.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.hacker.project.hackernewsproj.R;
import com.hacker.project.hackernewsproj.adapter.CardNewsAdapter;
import com.hacker.project.hackernewsproj.data.Constants;
import com.hacker.project.hackernewsproj.data.JsonData;
import com.hacker.project.hackernewsproj.data.Utils;

import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView refreshImage;
    private TextView titleText;

    private LinearLayout hackerNewsLayout;
    private LinearLayout newsLayout;
    private LinearLayout showLayout;
    private LinearLayout askLayout;
    private LinearLayout jobsLayout;

    private DrawerLayout mDrawerLayout;
    private View mDrawer;
    private TextView noStories;

    private Firebase preURL;
    private Firebase mainURL;
    private ArrayList<Long> submissionIDs;
    private int loadedSubmissions = 0;
    private int submissionUpdateNum = 15;

    ProgressDialog progressDialog;

    String headerType;
    String headerText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        setUpLayout();

        headerType = getIntent().getStringExtra(DATA_TYPE);
        if (headerType == null || headerType.isEmpty())
            headerType = "topstories";

        headerText = headerType.substring(0, headerType.length() - "stories".length()).toUpperCase();
        titleText.setText("Hacker News " + headerText);

        preURL = new Firebase(Constants.BASE_URL);
        mainURL = preURL.child("/" + headerType);

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
                    loadedSubmissions = 0;
                    // Because we are doing this asynchronously, it's easier to update submissions directly
                    updateSubmissions();

                    noStories.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    mRecyclerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.err.println("Could not retrieve posts! " + firebaseError);
                    noStories.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    mRecyclerView.setVisibility(View.GONE);
                }
            });
        } else {
            loadSubmissions();
        }
    }

    private void loadSubmissions() {

        // We cannot use feedAdapter.getCount() directly since that may lead to race conditions
        int start = loadedSubmissions;

        // From the top 500 submissions, we only load a few at a time
        for (; loadedSubmissions < start + submissionUpdateNum && loadedSubmissions < submissionIDs.size(); loadedSubmissions++) {
            // Add each submission to the view manually
            updateSingleSubmission(submissionIDs.get(loadedSubmissions));
        }

    }

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

                JsonData f = initNewFeedItem(submissionId, ret);
                ((CardNewsAdapter) mAdapter).addData(f);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.err.println("Could not retrieve post! " + firebaseError);
            }
        });
    }

    // Takes the raw API data and the URL, returns a new feed item
    public JsonData initNewFeedItem(Long submissionId, Map<String, Object> ret) {

        JsonData f = new JsonData();

        f.setId(submissionId);
        f.setKids((List<Integer>) ret.get("kids"));

        // Gets readable date
        String time = Utils.updateDate(ret.get("time").toString());

        // Set titles and other data
        f.setTitle((String) ret.get("title"));
        f.setText((String) ret.get("text"));
        f.setBy((String) ret.get("by"));
        f.setScore((Long) ret.get("score"));
        f.setTime(time);
        f.setUrl((String) ret.get("url"));

        // Jobs stories don't have any descendants, we need to take care of that
        Object descendantObject = ret.get("descendants");
        if (descendantObject != null) {
            f.setDescendants((Long) descendantObject);
        } else {
            System.err.println("Null descendants: " + ret.get("title"));
            f.setDescendants(0L);
        }

        return f;
    }


    @Override
    public void onClick(View v) {

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        int position = 0;
        if (v.getTag() != null)
            position = (int) v.getTag();

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
            case R.id.comment_holder:
                intent = new Intent(MainActivity.this, CommentsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("DESCENDANTS", ((CardNewsAdapter) mAdapter).getData(position).getDescendants().toString());
                bundle.putIntegerArrayList("KIDS", (ArrayList<Integer>) ((CardNewsAdapter) mAdapter).getData(position).getKids());
                bundle.putLong("ID", ((CardNewsAdapter) mAdapter).getData(position).getId());
                bundle.putString("TIME", ((CardNewsAdapter) mAdapter).getData(position).getTime());
                bundle.putString("TITLE", ((CardNewsAdapter) mAdapter).getData(position).getTitle());
                bundle.putString("AUTHOR", ((CardNewsAdapter) mAdapter).getData(position).getBy());
                intent.putExtra("BUNDLE", bundle);
                break;

            case R.id.url_holder:
            case R.id.news_title_tv:
                if (((CardNewsAdapter) mAdapter).getURL(position) != null && !((CardNewsAdapter) mAdapter).getURL(position).isEmpty()) {
                    intent = new Intent(MainActivity.this, FullNewsActivity.class);
                    intent.putExtra("URL", ((CardNewsAdapter) mAdapter).getURL(position));
                } else {
                    intent = null;
                    Toast.makeText(MainActivity.this, "No URL", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.load_more:
                intent = null;
                loadSubmissions();
                break;
            case R.id.drawermenu:
                intent = null;
                mDrawerLayout.openDrawer(mDrawer);
                break;
            case R.id.menu_refresh:
                intent = null;
                submissionIDs = null;
                loadedSubmissions = 0;
                ((CardNewsAdapter) mAdapter).clearData();
                updateSubmissions();
                break;


        }
        if (intent != null) {
            startActivity(intent);
            closeDrawer();
        }

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
        refreshImage = (ImageView) findViewById(R.id.menu_refresh);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer = findViewById(R.id.navigation_drawer);
        hackerNewsLayout = (LinearLayout) findViewById(R.id.ll_hacker_news);
        newsLayout = (LinearLayout) findViewById(R.id.ll_news);
        showLayout = (LinearLayout) findViewById(R.id.ll_show);
        askLayout = (LinearLayout) findViewById(R.id.ll_ask);
        jobsLayout = (LinearLayout) findViewById(R.id.ll_job);
        mRecyclerView = (RecyclerView) findViewById(R.id.news_list);
        mDrawer = (View) findViewById(R.id.navigation_drawer);
        noStories = (TextView) findViewById(R.id.no_stories_tv);
        titleText = (TextView) findViewById(R.id.header_text);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);

        hackerNewsLayout.setOnClickListener(this);
        newsLayout.setOnClickListener(this);
        showLayout.setOnClickListener(this);
        askLayout.setOnClickListener(this);
        jobsLayout.setOnClickListener(this);
        refreshImage.setOnClickListener(this);

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
