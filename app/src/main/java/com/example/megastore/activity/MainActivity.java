package com.example.megastore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.megastore.Dialog.SignUpDialog;
import com.example.megastore.R;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Button btn_Sign_up, btn_Login;
    TextInputLayout email_Login, password_Login;
    String str_Email_Login, str_Password_Login;

//    LoadingDialog loadingDialog;

    // Fire Base
    FirebaseAuth firebaseAuth;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progress);

        Circle circle = new Circle();
        progressBar.setIndeterminateDrawable(circle);
        progressBar.setVisibility(View.INVISIBLE);

        password_Login = findViewById(R.id.password_login);
        email_Login = findViewById(R.id.email_login);
        btn_Login = findViewById(R.id.btn_login);
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        final SignUpDialog dialog = new SignUpDialog(MainActivity.this);
        btn_Sign_up = findViewById(R.id.btn_sign_up);
        btn_Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        dialog.btn_Sign_Up_Now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.signUp();
            }
        });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (firebaseAuth != null){
//            startActivity(new Intent(MainActivity.this , HomeActivity.class));
//        }else {
//            Toast.makeText(this, "Login", Toast.LENGTH_LONG).show();
//        }
//    }

    private boolean validationEmail() {
        str_Email_Login = email_Login.getEditText().getText().toString();
        if (str_Email_Login.isEmpty()) {
            email_Login.setError("Enter Your Email");
            return false;
        } else if (!isEmailValid(str_Email_Login)) {
            email_Login.setError("Enter Correct Email example@example.com");
            return false;
        } else {
            email_Login.setError(null);
            return true;
        }
    }

    public boolean validationPassword() {
        str_Password_Login = password_Login.getEditText().getText().toString();
        if (str_Password_Login.isEmpty()) {
            password_Login.setError("Please Enter Your Password");
            return false;
        } else if (str_Password_Login.length() < 6) {
            password_Login.setError("The Password Should Be More Than 6 Digits");
            return false;
        } else {
            password_Login.setError(null);
            return true;
        }
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void login() {


        if (!validationEmail()) {
            email_Login.requestFocus();
        } else if (!validationPassword()) {
            password_Login.requestFocus();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(str_Email_Login, str_Password_Login)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                progressBar.setVisibility(View.INVISIBLE);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Email or Password Incorrect", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

        }

    }

}
