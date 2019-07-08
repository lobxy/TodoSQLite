package com.lobxy.todo_sqlite.DatabaseClasses;

import android.provider.BaseColumns;

public class DBContract {
    public DBContract() {
    }

    public class NotesEntry implements BaseColumns {
        public static final String DB_TABLE_NAME = "Notes";
        public static final String COLOUMN_TITLE = "Title";
        public static final String COLOUMN_DESC = "Description";
    }

    public class UserEntry implements BaseColumns {
        public static final String DB_TABLE_NAME = "Users";
        public static final String COLOUMN_NAME = "Name";
        public static final String COLOUMN_USERNAME = "Username";
        public static final String COLOUMN_PASSWORD = "Passoword";
    }

}
