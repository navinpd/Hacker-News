package com.hacker.project.hackernewsproj.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hacker.project.hackernewsproj.R;

/**
 * @author Navin Prasad <navin.prasad@fastacash.com>
 * @since 17/May/2016
 */


public class CardHolder extends RecyclerView.ViewHolder {

    public TextView newsTitleTV, timeDurationTV, authorTV, urlLinkTV, commentsTV, clickToLoad ;
    public CardView cardHolder;
    public RelativeLayout urlHolder;

    public CardHolder(View itemView) {
        super(itemView);

        newsTitleTV = (TextView) itemView.findViewById(R.id.news_title_tv);
        timeDurationTV = (TextView) itemView.findViewById(R.id.time_tv);
        authorTV = (TextView) itemView.findViewById(R.id.author_tv);
        urlLinkTV = (TextView) itemView.findViewById(R.id.hard_url_tv);
        clickToLoad = (TextView) itemView.findViewById(R.id.load_more);
        commentsTV = (TextView) itemView.findViewById(R.id.comment_holder);
        cardHolder = (CardView) itemView.findViewById(R.id.card_holder);
        urlHolder = (RelativeLayout) itemView.findViewById(R.id.url_holder);

    }

}