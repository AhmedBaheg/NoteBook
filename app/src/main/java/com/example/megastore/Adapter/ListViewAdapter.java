package com.example.megastore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.megastore.Model.Note;
import com.example.megastore.R;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<Note> {

    private Context context;
    private int resource;
    public TextView tv_Title;

    public ListViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Note> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.list_item_notes, parent , false);

        tv_Title = view.findViewById(R.id.tv_title);

        Note note  = getItem(position);
        tv_Title.setText(getItem(position).getTitle());

        return view;

//        String title=getItem(position).getTitle();
//        LayoutInflater inflater=LayoutInflater.from(context);
//        view =inflater.inflate(resource,parent,false);
//        tv_Title=view.findViewById(R.id.tv_title);
//        tv_Title.setText(title);
//        return view;

    }
}
