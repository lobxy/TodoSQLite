package com.lobxy.todo_sqlite.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lobxy.todo_sqlite.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {

    public TextView text_title;
    public TextView text_desc;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        text_desc = itemView.findViewById(R.id.list_desc);
        text_title = itemView.findViewById(R.id.list_title);
    }

}
