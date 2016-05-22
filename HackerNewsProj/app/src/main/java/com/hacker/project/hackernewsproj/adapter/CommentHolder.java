package com.hacker.project.hackernewsproj.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hacker.project.hackernewsproj.R;

/**
 * Created by Navin on 19/05/16.
 */
public class CommentHolder extends RecyclerView.ViewHolder {
    TextView author, comment, time;

    public CommentHolder(View itemView) {
        super(itemView);
        author = (TextView) itemView.findViewById(R.id.comment_writer_tv);
        comment = (TextView) itemView.findViewById(R.id.comment_tv);
        time = (TextView) itemView.findViewById(R.id.comment_time_tv);
    }

}
