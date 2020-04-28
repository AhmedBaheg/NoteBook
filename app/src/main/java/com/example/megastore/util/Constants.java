package com.example.megastore.util;

import com.google.firebase.auth.FirebaseAuth;

public class Constants {

    public static final String USERS = "Users";
    public static final String NOTES = "Notes";

    public static String getUID() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
