package com.hacker.project.hackernewsproj.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.Map;

/**
 * @author Navin Prasad <navin.prasad@fastacash.com>
 * @since 19/May/2016
 */


public class CommentsActivity extends BaseActivity {
    TextView titleText;
    TextView timeText;
    TextView authorText;
    TextView commentCounts;
    private long id;
    private ArrayList<Long> submissionIDs;

    String title, comment, time, submissionId;

    RecyclerView commentsList;

    ProgressDialog progressDialog;
    int loadedSubmissions = 0;
    int submissionUpdateNum = 15;


    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        title = (String) getIntent().getStringExtra("TITLE");


        setUpLayout();


        fillLayout();

        updateSubmissions();

    }


    public void updateSubmissions() {
        if (submissionIDs == null) {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.show();

            Firebase mainURL = new Firebase("https://hacker-news.firebaseio.com/v0/item/" + id);

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

        JsonData f = (JsonData) ret;

        f.setId(submissionId);

        // Gets readable date
        String time = Utils.updateDate((String) ret.get("time"));

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

    private void fillLayout() {
        timeText.setText(Utils.updateDate(jsonData.getTime()));
        titleText.setText(String.valueOf(jsonData.getTitle()));
        commentCounts.setText(String.valueOf(jsonData.getScore()));
        id = jsonData.getId();

    }

    private void setUpLayout() {
        titleText = (TextView) findViewById(R.id.title_text);
        timeText = (TextView) findViewById(R.id.time_text);
        commentsList = (RecyclerView) findViewById(R.id.comment_list);
//        authorText = (TextView) findViewById(R.id.writer_tv);
        commentCounts = (TextView) findViewById(R.id.comment_number);

        progressDialog = new ProgressDialog(CommentsActivity.this);
        progressDialog.setMessage("Loading");

        layoutManager = new LinearLayoutManager(this);
        commentsList.setLayoutManager(layoutManager);
        commentsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mAdapter = new CommentsAdapter(CommentsActivity.this, null);
        commentsList.setAdapter(mAdapter);

    }
}
