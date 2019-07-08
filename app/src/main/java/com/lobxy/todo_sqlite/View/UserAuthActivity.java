package com.lobxy.todo_sqlite.View;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lobxy.todo_sqlite.DatabaseClasses.DBContract;
import com.lobxy.todo_sqlite.DatabaseClasses.DBHelper;
import com.lobxy.todo_sqlite.PrefManagers;
import com.lobxy.todo_sqlite.R;

public class UserAuthActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    private PrefManagers prefManagers;

    private EditText edit_name;
    private EditText edit_username;
    private EditText edit_password;

    private Button btn_trigger_login;
    private Button btn_trigger_register;

    private boolean login_trigger = true; //true for login and false for register

    private String name = "", username = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_auth);

        dbHelper = new DBHelper(this);
        prefManagers = new PrefManagers(this);

        edit_name = findViewById(R.id.auth_edit_name);
        edit_username = findViewById(R.id.auth_edit_username);
        edit_password = findViewById(R.id.auth_edit_password);

        btn_trigger_login = findViewById(R.id.auth_btn_trigger_login);
        btn_trigger_register = findViewById(R.id.auth_btn_trigger_register);

        Button btn_submit = findViewById(R.id.auth_btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edit_username.getText().toString().trim();
                password = edit_password.getText().toString().trim();

                if (login_trigger) {
                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                        Toast.makeText(UserAuthActivity.this, "Field empty", Toast.LENGTH_SHORT).show();
                    } else {
                        loginUser();
                    }
                } else {
                    name = edit_name.getText().toString().trim();

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                        Toast.makeText(UserAuthActivity.this, "Field empty", Toast.LENGTH_SHORT).show();
                    } else {
                        if (checkIfUserExists()) {
                            Toast.makeText(UserAuthActivity.this, "User Already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            saveUserIntoDatabase();
                        }
                    }
                }
            }
        });

        btn_trigger_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_trigger = false;
                edit_name.setVisibility(View.VISIBLE);
                btn_trigger_register.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                btn_trigger_login.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                edit_name.setText("");
                edit_username.setText("");
                edit_password.setText("");
            }
        });

        btn_trigger_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_trigger = true;
                edit_name.setVisibility(View.GONE);
                btn_trigger_login.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                btn_trigger_register.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                edit_name.setText("");
                edit_username.setText("");
                edit_password.setText("");
            }
        });

    }

    private void loginUser() {
        database = dbHelper.getReadableDatabase();

        //data to fetch.
        String[] columns = {DBContract.UserEntry.COLOUMN_USERNAME};

        //selection criteria
        String criteria = DBContract.UserEntry.COLOUMN_USERNAME + " = ?" + " AND " + DBContract.UserEntry.COLOUMN_PASSWORD + " = ? ";

        //arguments
        String[] arguments = {username, password};

        Cursor cursor = database.query(
                DBContract.UserEntry.DB_TABLE_NAME,
                columns,
                criteria,
                arguments,
                null,
                null,
                null
        );

        int cursorCount = cursor.getCount();

        cursor.close();
        database.close();

        if (cursorCount > 0) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkIfUserExists() {
        //check weather the user already exists in the database.

        database = dbHelper.getReadableDatabase();

        //array of columns to fetch.
        String[] columns = {DBContract.UserEntry.COLOUMN_USERNAME};

        //selection criteria.
        String selectionCriteria = DBContract.UserEntry.COLOUMN_USERNAME + " = ?";

        //selection argument
        String[] argument = {username};

        //select username from USER where username = edit.getText.
        Cursor cursor = database.query(
                DBContract.UserEntry.DB_TABLE_NAME, //Table to query
                columns,                            //columns to return
                selectionCriteria,                  //columns for the WHERE clause
                argument,                           //The values for the WHERE clause
                null,                       //group the rows
                null,                        //filter by row groups
                null                        //The sort order
        );

        int cursorCountResult = cursor.getCount();
        cursor.close();
        database.close();

        return cursorCountResult > 0;

    }

    private void saveUserIntoDatabase() {
        database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.UserEntry.COLOUMN_USERNAME, username);
        contentValues.put(DBContract.UserEntry.COLOUMN_NAME, name);
        contentValues.put(DBContract.UserEntry.COLOUMN_PASSWORD, password);

        database.insert(DBContract.UserEntry.DB_TABLE_NAME, null, contentValues);
        database.close();

        prefManagers.saveLoginDetails(username);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (prefManagers.userAlreadyLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "User Logged out", Toast.LENGTH_SHORT).show();
        }
    }
}
