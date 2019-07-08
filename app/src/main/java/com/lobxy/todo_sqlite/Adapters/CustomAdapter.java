package com.lobxy.todo_sqlite.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lobxy.todo_sqlite.Model.Notes;
import com.lobxy.todo_sqlite.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private List<Notes> notesList;

    public CustomAdapter(List<Notes> notesList) {
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.notes_list_item, null, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int i) {
        holder.text_title.setText(notesList.get(i).getTitle());
        holder.text_desc.setText(notesList.get(i).getDescription());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
