package com.example.megastore.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.megastore.Model.Note;
import com.example.megastore.Model.User;
import com.example.megastore.R;
import com.example.megastore.util.Constants;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpDialog extends Dialog {

    public TextInputLayout first_Name, last_Name, email, password, confirm_Password;
    public String str_First_Name, str_Last_Name, str_Email, str_Password, str_Confirm_Password;
    public Button btn_Cancel, btn_Sign_Up_Now;

    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;

    public ProgressBar progressBar;

    public SignUpDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.sign_up_dialog);
        setCancelable(false);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        progressBar = findViewById(R.id.progress);

        Circle circle = new Circle();
        progressBar.setIndeterminateDrawable(circle);
        progressBar.setVisibility(View.INVISIBLE);

        first_Name = findViewById(R.id.first_name);
        last_Name = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_Password = findViewById(R.id.confirm_password);
        btn_Sign_Up_Now = findViewById(R.id.btn_sign_up_now);
        btn_Cancel = findViewById(R.id.btn_cancel);

        // Fire Base
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Constants.USERS);
        firebaseAuth = FirebaseAuth.getInstance();

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public boolean validationFirstName() {
        str_First_Name = first_Name.getEditText().getText().toString();
        if (str_First_Name.isEmpty()) {
            first_Name.setError("Please Enter Your First Name");
            return false;
        } else if (!PATTERN_NAME.matcher(str_First_Name).matches()) {
            first_Name.setError("Please enter alphabet letters only");
            return false;
        } else {
            first_Name.setError(null);
            return true;
        }
    }

    public boolean validationLastName() {
        str_Last_Name = last_Name.getEditText().getText().toString();
        if (str_Last_Name.isEmpty()) {
            last_Name.setError("Please Enter Your First Name");
            return false;
        } else if (!PATTERN_NAME.matcher(str_Last_Name).matches()) {
            last_Name.setError("Please enter alphabet letters only");
            return false;
        } else {
            last_Name.setError(null);
            return true;
        }
    }

    private boolean validationEmail() {
        str_Email = email.getEditText().getText().toString();
        if (str_Email.isEmpty()) {
            email.setError("Enter Your Email");
            return false;
        } else if (!isEmailValid(str_Email)) {
            email.setError("Enter Correct Email example@example.com");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    public boolean validationPassword() {
        str_Password = password.getEditText().getText().toString();
        if (str_Password.isEmpty()) {
            password.setError("Please Enter Your Password");
            return false;
        }else if (str_Password.length() < 6){
            password.setError("The Password Should Be More Than 6 Digits");
            return false;
        }else {
            password.setError(null);
            return true;
        }
    }

    public boolean validationConfirmPassword(){
        str_Confirm_Password = confirm_Password.getEditText().getText().toString();
        str_Password = password.getEditText().getText().toString();
        if (str_Confirm_Password.isEmpty()){
            confirm_Password.setError("Please Confirm Password");
            return false;
        }else if (!str_Confirm_Password.equals(str_Password)){
            confirm_Password.setError("The Two Password Not Equals");
            return false;
        }else {
            confirm_Password.setError(null);
            return true;
        }
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private final static Pattern PATTERN_NAME = Pattern.compile("[\\u0600-\\u065F\\u066A-\\u06EF\\u06FA-\\u06FFa-zA-Z ]+[\\u0600-\\u065F\\u066A-\\u06EF\\u06FA-\\u06FFa-zA-Z-_ ]");

    public void signUp(){

        if (!validationFirstName()){
            first_Name.requestFocus();
        }else if (!validationLastName()){
            last_Name.requestFocus();
        }else if (!validationEmail()){
            email.requestFocus();
        }else if (!validationPassword()){
            password.requestFocus();
        }else if (!validationConfirmPassword()){
            confirm_Password.requestFocus();
        }else {

            progressBar.setVisibility(View.VISIBLE);

            final String fullName = str_First_Name + " " + str_Last_Name;

            firebaseAuth.createUserWithEmailAndPassword(str_Email , str_Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

                                User user = new User(fullName , str_Email);
                                databaseReference.child(Constants.getUID())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        dismiss();

                                    }
                                });

                            }else {
                                String message = task.getException().getMessage();
                                Toast.makeText(getContext(), "Error Occurred " + message, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                        }
                    });

        }

    }

}
