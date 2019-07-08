package com.lobxy.todo_sqlite;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManagers {
    private Context context;

    public PrefManagers(Context context) {
        this.context = context;
    }

    public void saveLoginDetails(String username) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    public void clearLoginDetails() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.apply();
    }

    public boolean userAlreadyLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("username", "");
        if (user.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }


}
