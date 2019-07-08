package com.lobxy.todo_sqlite.View;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lobxy.todo_sqlite.Adapters.CustomAdapter;
import com.lobxy.todo_sqlite.DatabaseClasses.DBContract;
import com.lobxy.todo_sqlite.DatabaseClasses.DBHelper;
import com.lobxy.todo_sqlite.Model.Notes;
import com.lobxy.todo_sqlite.PrefManagers;
import com.lobxy.todo_sqlite.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase database;

    private EditText edit_title;
    private EditText edit_desc;

    DBHelper dbHelper;

    List<Notes> notesList;

    RecyclerView recyclerView;

    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesList = new ArrayList<>();
        recyclerView = findViewById(R.id.main_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);

        edit_desc = findViewById(R.id.main_edit_desc);
        edit_title = findViewById(R.id.main_edit_title);

        Button submit = findViewById(R.id.main_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataIntoDatabase();
            }
        });
    }

    private void saveDataIntoDatabase() {
        database = dbHelper.getWritableDatabase();

        String title = edit_title.getText().toString().trim();
        String desc = edit_desc.getText().toString().trim();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.NotesEntry.COLOUMN_TITLE, title);
        contentValues.put(DBContract.NotesEntry.COLOUMN_DESC, desc);

        database.insert(DBContract.NotesEntry.DB_TABLE_NAME, null, contentValues);

        fetchDataFromDatabase();
    }

    private void fetchDataFromDatabase() {
        notesList.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DBContract.NotesEntry.DB_TABLE_NAME,
                new String[]{DBContract.NotesEntry._ID, DBContract.NotesEntry.COLOUMN_TITLE, DBContract.NotesEntry.COLOUMN_DESC},
                null, null, null, null, null);

        while (cursor.moveToNext()) {

            String title = cursor.getString(cursor.getColumnIndex(DBContract.NotesEntry.COLOUMN_TITLE));
            String desc = cursor.getString(cursor.getColumnIndex(DBContract.NotesEntry.COLOUMN_DESC));
            int id = cursor.getColumnIndex(DBContract.NotesEntry._ID);

            Log.i("main", "fetchDataFromDatabase: title: " + title + "\ndesc: " + desc);

            notesList.add(new Notes(id, title, desc));
        }

        if (adapter == null) {
            adapter = new CustomAdapter(notesList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchDataFromDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_menu_logout) {
            PrefManagers prefManagers = new PrefManagers(this);
            prefManagers.clearLoginDetails();
            startActivity(new Intent(this, UserAuthActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
