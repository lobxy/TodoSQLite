package com.lobxy.todo_sqlite.DatabaseClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_NOTE_TABLE = "CREATE TABLE " + DBContract.NotesEntry.DB_TABLE_NAME + " (" +
            DBContract.NotesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.NotesEntry.COLOUMN_TITLE + "  TEXT NOT NULL, " +
            DBContract.NotesEntry.COLOUMN_DESC + "  INTEGER NOT NULL" +
            ");";
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + DBContract.UserEntry.DB_TABLE_NAME + " (" +
            DBContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.UserEntry.COLOUMN_NAME + "  TEXT NOT NULL, " +
            DBContract.UserEntry.COLOUMN_USERNAME + "  TEXT NOT NULL," +
            DBContract.UserEntry.COLOUMN_PASSWORD + "  TEXT NOT NULL" +
            ");";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("DBHELPER", "onUpgrade: Dropped table " + oldVersion + " for new version " + newVersion + " and deleted all data.");
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.NotesEntry.DB_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.UserEntry.DB_TABLE_NAME);
        onCreate(db);
    }
}
