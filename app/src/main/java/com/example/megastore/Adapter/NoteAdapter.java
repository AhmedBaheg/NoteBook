package com.example.megastore.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.megastore.Model.Note;
import com.example.megastore.R;
import com.example.megastore.activity.Content;
import com.example.megastore.activity.HomeActivity;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Note> noteList;
//    private OnNoteItemListener onNoteItemListener;
    public static String id;
    public static String title;
    private HomeActivity homeActivity;

//    public interface OnNoteItemListener {
//        void onNoteItemListener(int position);
//    }
//
//    public void setOnNoteItemListener(OnNoteItemListener onNoteItemListener) {
//        this.onNoteItemListener = onNoteItemListener;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tv_Title , tv_date;

        private ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tv_Title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Note note = noteList.get(position);
            id = note.getId();
            homeActivity.updateNotes();
//            Intent intent = new Intent(homeActivity, Content.class);
//            intent.putExtra("id", id);
//            homeActivity.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            Note note = noteList.get(position);
            id = note.getId();
            title = note.getTitle();
            homeActivity.deleteNotes();
            return false;
        }
    }

    public NoteAdapter(Context context, ArrayList<Note> noteList, HomeActivity homeActivity) {
        this.context = context;
        this.noteList = noteList;
        this.homeActivity = homeActivity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_notes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        Note note = noteList.get(position);
        holder.tv_Title.setText(noteList.get(position).getTitle());
        holder.tv_date.setText(noteList.get(position).getDate());
        noteList.get(position);

    }


    @Override
    public int getItemCount() {
        return noteList.size();
    }


}
