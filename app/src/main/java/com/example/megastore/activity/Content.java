package com.example.megastore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.megastore.Adapter.NoteAdapter;
import com.example.megastore.Dialog.AddNoteDialog;
import com.example.megastore.Model.Note;
import com.example.megastore.R;
import com.example.megastore.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Content extends AppCompatActivity {

    EditText ed_Title , ed_Note;
    Note note;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);



        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Constants.NOTES);

        ed_Title = findViewById(R.id.ed_title);
        ed_Note = findViewById(R.id.ed_note);
//        String id = getIntent().getStringExtra("id");

//        returnData();
//        returnDataInDialog();

    }

    private void returnDataInDialog() {

        final AddNoteDialog dialog = new AddNoteDialog(this);
        databaseReference.child(NoteAdapter.id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                note = dataSnapshot.getValue(Note.class);
//                databaseReference = firebaseDatabase.getReference(Constants.NOTES).child(note.getId());
                dialog.input_Note_Title = note.getTitle();
                dialog.input_Note_Body = note.getNote();
                dialog.note_Title.getEditText().setText(dialog.input_Note_Title);
                dialog.note_Body.getEditText().setText(dialog.input_Note_Body);
                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void returnData(){

        databaseReference.child(NoteAdapter.id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                note = dataSnapshot.getValue(Note.class);
                ed_Title.setText(note.getTitle());
                ed_Note.setText(note.getNote());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
