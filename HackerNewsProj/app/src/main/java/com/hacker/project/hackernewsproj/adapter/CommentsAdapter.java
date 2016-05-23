package com.hacker.project.hackernewsproj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hacker.project.hackernewsproj.R;
import com.hacker.project.hackernewsproj.activity.CommentsActivity;
import com.hacker.project.hackernewsproj.data.JsonData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Navin on 19/05/16.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentHolder> {

    private List<JsonData> mLinks;
    private Context mContext;
    CommentsActivity activity;


    public CommentsAdapter(Context context, List<JsonData> links) {
        mContext = context;
        if (links != null)
            mLinks = links;
        else
            mLinks = new ArrayList<>();
        activity = (CommentsActivity) mContext;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_comments, parent, false);

        CommentHolder cardHolder = new CommentHolder(view);
        return cardHolder;
    }

    public void addToList(JsonData data) {
        mLinks.add(data);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        if (mLinks == null || mLinks.size() <= position || mLinks.get(position) == null)
            return;

        holder.time.setText(mLinks.get(position).getTime());
        if (mLinks.get(position).getText() != null)
            holder.comment.setText(Html.fromHtml(mLinks.get(position).getText()).toString());
        holder.author.setText(mLinks.get(position).getBy());
    }

    @Override
    public int getItemCount() {
        return mLinks.size();
    }
}
