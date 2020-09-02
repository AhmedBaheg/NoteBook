package com.example.megastore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.megastore.R;
import com.example.megastore.util.Constants;
import com.example.megastore.util.PreferenceUtils;
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

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        text_Splash = findViewById(R.id.text_splash);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
        text_Splash.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingSplash();
            }
        }, 3500);

    }

    private void loadingSplash() {

        if (PreferenceUtils.getEmail(this) != null || PreferenceUtils.getPassword(this) != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        } else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

//        if (firebaseUser != null) {
//            if (firebaseUser.isEmailVerified()) {
//                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//                finish();
//            } else {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                finish();
//            }
//        // if( firebaseUser == null )
//        } else {
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

    }
}


