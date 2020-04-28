package com.example.megastore.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.megastore.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_Title;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_Title = itemView.findViewById(R.id.tv_title);

    }
}
