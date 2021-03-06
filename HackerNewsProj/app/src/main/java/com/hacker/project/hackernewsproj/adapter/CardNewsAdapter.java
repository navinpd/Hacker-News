package com.hacker.project.hackernewsproj.adapter;

/**
 * Created by nagarjuna-1383 on 27/8/15.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hacker.project.hackernewsproj.R;
import com.hacker.project.hackernewsproj.activity.MainActivity;
import com.hacker.project.hackernewsproj.data.JsonData;

import java.util.ArrayList;
import java.util.List;


public class CardNewsAdapter extends RecyclerView.Adapter<CardHolder> {

    private List<JsonData> mLinks;
    private Context mContext;
    MainActivity activity;

    public CardNewsAdapter(Context context, List<JsonData> links) {
        mContext = context;
        if (links != null)
            mLinks = links;
        else
            mLinks = new ArrayList<>();
        activity = (MainActivity) mContext;
    }

    public void clearData() {
        mLinks = null;
    }

    public void addData(JsonData newData) {
        if(mLinks == null)
            mLinks = new ArrayList<>();
        mLinks.add(newData);
    }

    public String getURL(int position) {
        return mLinks.get(position).getUrl();
    }

    public JsonData getData(int position) {
        return mLinks.get(position);
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

        if (position == mLinks.size()) {
            holder.cardHolder.setVisibility(View.GONE);
            holder.clickToLoad.setVisibility(View.VISIBLE);
            holder.clickToLoad.setOnClickListener(activity);
        } else {

            holder.cardHolder.setVisibility(View.VISIBLE);
            holder.clickToLoad.setVisibility(View.GONE);
            holder.urlHolder.setTag(position);
            holder.newsTitleTV.setTag(position);
            holder.commentsTV.setTag(position);

            holder.urlHolder.setOnClickListener(activity);
            holder.newsTitleTV.setOnClickListener(activity);
            holder.commentsTV.setOnClickListener(activity);

            fillData(holder, position);
        }

        if(position == 442) {
            holder.cardHolder.setVisibility(View.GONE);
            holder.clickToLoad.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (mLinks != null && mLinks.size() > 0)
            return mLinks.size() + 1;
        else
            return 0;
    }

    //set name, image and channel
    private void fillData(CardHolder holder, int position) {
        if (mLinks != null && mLinks.get(position) != null) {

            JsonData data = mLinks.get(position);
            holder.urlLinkTV.setText(data.getUrl());
            holder.commentsTV.setText(data.getDescendants().toString());
            holder.newsTitleTV.setText(data.getTitle());
            holder.timeDurationTV.setText(String.valueOf(data.getTime()));
            holder.authorTV.setText(data.getBy());
        }
    }


}