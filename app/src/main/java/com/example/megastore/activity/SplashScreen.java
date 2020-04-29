package com.example.megastore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.megastore.R;
import com.example.megastore.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    Animation animation;
    TextView text_Splash;

    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        text_Splash = findViewById(R.id.text_splash);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Constants.USERS);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
        text_Splash.startAnimation(animation);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    // go to the main activity
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    // kill current activity
                    finish();
                }
            };
            // Show splash screen for 3 seconds
            new Timer().schedule(task, 5000);
        } else {
            updateUserUI();
        }

    }

    private void updateUserUI(){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    }
                };
                new Timer().schedule(task , 5000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
