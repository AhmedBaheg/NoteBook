package com.example.megastore.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.megastore.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddNoteDialog extends Dialog {

    public TextInputLayout note_Title , note_Body;
    public Button btn_Cancel , btn_Save;
    public String input_Note_Title , input_Note_Body;

    public AddNoteDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.add_note_dialog);
        setCancelable(false);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        note_Title = findViewById(R.id.note_title);
        note_Body = findViewById(R.id.note_body);
        btn_Cancel = findViewById(R.id.btn_cancel);
        btn_Save = findViewById(R.id.btn_save);

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public boolean validationTitle(){
        input_Note_Title = note_Title.getEditText().getText().toString();

        if (input_Note_Title.isEmpty()){
            note_Title.setError("Please Enter Title Note");
            return false;
        }else {
            note_Title.setError(null);
            return true;
        }
    }

    public boolean validationBody(){
        input_Note_Body = note_Body.getEditText().getText().toString();

        if (input_Note_Body.isEmpty()){
            note_Body.setError("Please Enter Body Note");
            return false;
        }else {
            note_Body.setError(null);
            return true;
        }
    }

}
