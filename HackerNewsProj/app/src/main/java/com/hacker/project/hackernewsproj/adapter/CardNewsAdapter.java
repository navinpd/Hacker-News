package com.hacker.project.hackernewsproj.adapter;

/**
 * Created by nagarjuna-1383 on 27/8/15.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hacker.project.hackernewsproj.NewsData;
import com.hacker.project.hackernewsproj.R;
import com.hacker.project.hackernewsproj.activity.MainActivity;

import java.util.List;


public class CardNewsAdapter extends RecyclerView.Adapter<CardHolder> {

    private List<NewsData> mLinks;
    private Context mContext;

    public CardNewsAdapter(Context context, List<NewsData> links) {
        mContext = context;
        mLinks = links;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_adapter, parent, false);

        CardHolder cardHolder = new CardHolder(view);
        return cardHolder;
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {

        MainActivity activity = (MainActivity) mContext;
        holder.urlHolder.setTag(position);
        holder.newsTitleTV.setTag(position);
        holder.commentHolder.setTag(position);

        holder.urlHolder.setOnClickListener(activity);
        holder.newsTitleTV.setOnClickListener(activity);
        holder.commentHolder.setOnClickListener(activity);

        fillData(holder, position);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    //set name, image and channel
    private void fillData(CardHolder holder, int position) {
        if (mLinks != null && mLinks.get(position) != null) {

            NewsData data = mLinks.get(position);
            holder.urlLinkTV.setText(data.getUrlString());
            holder.commentsTV.setText(data.getComments());
            holder.newsTitleTV.setText(data.getHeading());
            holder.timeDurationTV.setText(String.valueOf(data.getTimePassed()));
            holder.authorTV.setText(data.getAuthorName());
        }
    }


}