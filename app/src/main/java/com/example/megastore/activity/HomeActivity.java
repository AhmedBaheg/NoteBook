package com.example.megastore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.megastore.Adapter.ListViewAdapter;
import com.example.megastore.Adapter.NoteAdapter;
import com.example.megastore.Dialog.AddNoteDialog;
import com.example.megastore.Dialog.UpdateNoteDialog;
import com.example.megastore.Model.Note;
import com.example.megastore.R;
import com.example.megastore.ViewHolder.NoteViewHolder;
import com.example.megastore.util.Constants;
import com.example.megastore.util.PreferenceUtils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    String currentDate;
    ImageView add_Note;
    RecyclerView recyclerView;
    FloatingActionButton btn_Log_Out;
    ListView listView;
    AddNoteDialog dialog;
    UpdateNoteDialog updateNoteDialog;
    Note note;
    public ArrayList<Note> arrayList;
    NoteAdapter noteAdapter;
    ListViewAdapter listViewAdapter;

    FirebaseRecyclerAdapter<Note, NoteViewHolder> myAdapter;
    FirebaseRecyclerOptions<Note> options;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Constants.USERS).child(Constants.getUID()).child(Constants.NOTES);
        databaseReference.keepSynced(true);

        currentDate = new SimpleDateFormat(" EEEE\n dd-MM-yyyy", Locale.getDefault()).format(new Date());

        add_Note = findViewById(R.id.add_note);
        add_Note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        btn_Log_Out = findViewById(R.id.log_out);
        btn_Log_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtils.saveEmail(null , HomeActivity.this);
                PreferenceUtils.savePassword(null , HomeActivity.this);
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        showDataInRecyclerView();
        displayNotesRecyclerView();

    }

    public void deleteNotes() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(NoteAdapter.title)
                .setMessage("Are You Sure From Delete: " + NoteAdapter.title)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child(NoteAdapter.id).removeValue();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    public void updateNotes() {

        updateNoteDialog = new UpdateNoteDialog(this);
        updateNoteDialog.show();
        databaseReference.child(NoteAdapter.id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                note = dataSnapshot.getValue(Note.class);
                if (note != null) {
                    updateNoteDialog.input_Note_Title = note.getTitle();
                    updateNoteDialog.note_Title.getEditText().setText(updateNoteDialog.input_Note_Title);
                    updateNoteDialog.note_Title.getEditText().setSelection(updateNoteDialog.note_Title.getEditText().getText().length());
                    updateNoteDialog.input_Note_Body = note.getNote();
                    updateNoteDialog.note_Body.getEditText().setText(updateNoteDialog.input_Note_Body);
                    updateNoteDialog.note_Body.getEditText().setSelection(updateNoteDialog.note_Body.getEditText().getText().length());
                }
                updateNoteDialog.btn_Update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!updateNoteDialog.validationTitle()) {
                            updateNoteDialog.note_Title.requestFocus();
                        } else if (!updateNoteDialog.validationBody()) {
                            updateNoteDialog.note_Body.requestFocus();
                        } else {
                            saveData(updateNoteDialog.input_Note_Title, updateNoteDialog.input_Note_Body, NoteAdapter.id, currentDate);
                            updateNoteDialog.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void saveData(String title, String body, String id, String date) {
        note = new Note(title, body, id, date);
        databaseReference.child(NoteAdapter.id).setValue(note);
    }

    private void displayNotesListView() {

        arrayList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear(); /** to delete data inside array list before inside in for each to not repetition value */
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    note = snapshot.getValue(Note.class);
//                    String title = note.getTitle();
                    arrayList.add(0, note);
                }
                listViewAdapter = new ListViewAdapter(HomeActivity.this, 0, arrayList);
                listView.setAdapter(listViewAdapter);

//                listViewAdapter =new ListViewAdapter(getApplicationContext(),R.layout.list_item_notes,arrayList);
//                listView.setAdapter(listViewAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void displayNotesRecyclerView() {

        arrayList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear(); /** to delete data inside array list before inside in for each to not repetition value */
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    note = snapshot.getValue(Note.class);
                    arrayList.add(0, note);
                }
                noteAdapter = new NoteAdapter(HomeActivity.this, HomeActivity.this, arrayList);
                recyclerView.setAdapter(noteAdapter);
//                returnDataInDialog();

//                noteAdapter.setOnNoteItemListener(new NoteAdapter.OnNoteItemListener() {
//                    @Override
//                    public void onNoteItemListener(int position) {
//                        Note note = arrayList.get(position);
//                        Toast.makeText(HomeActivity.this, note.getTitle(), Toast.LENGTH_LONG).show();
//                    }
//                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showDataInRecyclerView() {

        options = new FirebaseRecyclerOptions.Builder<Note>()
                .setQuery(databaseReference, Note.class)
                .build();
        myAdapter = new FirebaseRecyclerAdapter<Note, NoteViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull Note note) {
                final String key = getRef(i).getKey();
                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), Content.class);
                        intent.putExtra("id", key);
                        startActivity(intent);
                    }
                });
                noteViewHolder.tv_Title.setText(note.getTitle());
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notes, parent, false);
                return new NoteViewHolder(view);
            }

        };
        myAdapter.startListening();
        myAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(myAdapter);

    }


    public void showDialog() {

        dialog = new AddNoteDialog(HomeActivity.this);
        dialog.show();

        dialog.input_Note_Title = dialog.note_Title.getEditText().getText().toString();
        dialog.note_Title.getEditText().setSelection(dialog.note_Title.getEditText().getText().length());
        dialog.input_Note_Body = dialog.note_Body.getEditText().getText().toString();
        dialog.note_Body.getEditText().setSelection(dialog.note_Body.getEditText().getText().length());

        dialog.btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dialog.validationTitle()) {
                    dialog.note_Title.requestFocus();
                } else if (!dialog.validationBody()) {
                    dialog.note_Body.requestFocus();
                } else {

                    String id = databaseReference.push().getKey();

                    note = new Note(dialog.input_Note_Title, dialog.input_Note_Body, id, currentDate);
                    databaseReference.child(id).setValue(note);
                    dialog.dismiss();

                }
            }
        });

    }

}
