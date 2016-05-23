package com.hacker.project.hackernewsproj.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.hacker.project.hackernewsproj.R;
import com.hacker.project.hackernewsproj.adapter.CommentsAdapter;
import com.hacker.project.hackernewsproj.data.JsonData;
import com.hacker.project.hackernewsproj.data.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Navin Prasad <navin.prasad@fastacash.com>
 * @since 19/May/2016
 */


public class CommentsActivity extends BaseActivity {
    private TextView titleText;
    private TextView timeText;
    private TextView authorText;
    private TextView commentCounts;
    private TextView noCommentText;
    private ArrayList<Long> commentIds;

    private String title, time, author, commentCount;
    private long id;

    private RecyclerView commentsList;

    private ProgressDialog progressDialog;
    private int loadedSubmissions = 0;
    private int submissionUpdateNum = 15;


    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Bundle bundle = getIntent().getBundleExtra("BUNDLE");


        author = bundle.getString("AUTHOR");
        title = bundle.getString("TITLE");
        time = bundle.getString("TIME");
        id = bundle.getLong("ID", -1);
        commentIds = (ArrayList<Long>) bundle.get("KIDS");
        commentCount = bundle.getString("DESCENDANTS");

        setUpLayout();

        fillMainLayout();

        updateSubmissions();

    }

    public void updateSubmissions() {
        if (commentIds == null || commentIds.size() == 0) {
            noCommentText.setVisibility(View.VISIBLE);
            commentsList.setVisibility(View.GONE);
        } else {
            callSingleItems();
            noCommentText.setVisibility(View.GONE);
            commentsList.setVisibility(View.VISIBLE);
        }
    }

    private void callSingleItems() {

        // We cannot use feedAdapter.getCount() directly since that may lead to race conditions
        int start = loadedSubmissions;

        // From the top 500 submissions, we only load a few at a time
        for (; loadedSubmissions < start + submissionUpdateNum && loadedSubmissions < commentIds.size(); loadedSubmissions++) {
            // But we must first add each submission to the view manually
            updateSingleSubmission(commentIds.get(loadedSubmissions));
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
                ((CommentsAdapter) mAdapter).addToList(f);
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

        JsonData f = new JsonData();;

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

    private void fillMainLayout() {
        timeText.setText(time);
        titleText.setText(String.valueOf(title));
        authorText.setText(author);
        commentCounts.setText(commentCount);
    }

    private void setUpLayout() {
        titleText = (TextView) findViewById(R.id.title_text);
        timeText = (TextView) findViewById(R.id.time_text);
        commentsList = (RecyclerView) findViewById(R.id.comment_list);
        authorText = (TextView) findViewById(R.id.author_text);
        commentCounts = (TextView) findViewById(R.id.comment_number);
        noCommentText = (TextView) findViewById(R.id.no_comment_tv);

        progressDialog = new ProgressDialog(CommentsActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);

        layoutManager = new LinearLayoutManager(this);
        commentsList.setLayoutManager(layoutManager);

        mAdapter = new CommentsAdapter(CommentsActivity.this, null);
        commentsList.setAdapter(mAdapter);

    }
}
