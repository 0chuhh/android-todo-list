package com.example.todolist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todolist.Model.GroupModel;
import com.example.todolist.Model.TaskModel;

public class DataBaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "TODO_DATABASE";

    private static final String TABLE_NAME = "Groups";
    private static final String SECOND_TABLE_NAME = "Task";
    private static final String THIRD_TABLE_NAME = "Status";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                THIRD_TABLE_NAME +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SECOND_TABLE_NAME +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, group_id INTEGER REFERENCES "
                + TABLE_NAME + "(id), date_create TEXT NOT NULL, date_end TEXT, status_id INTEGER REFERENCES "
                + THIRD_TABLE_NAME + "(id))");

        db.execSQL("INSERT INTO " + THIRD_TABLE_NAME + " VALUES ('Ожидает выполнения', 'Выполнено', 'Просрочено')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SECOND_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + THIRD_TABLE_NAME);
        onCreate(db);
    }

    public void insertGroup(GroupModel model){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", model.getName());
        db.insert(TABLE_NAME, null, values);
    }

    public void insertTask(TaskModel model){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", model.getName());
        values.put("group_id", model.getGroup_id());
        values.put("date_create", model.getDate_create().toString());
        values.put("date_end", model.getDate_end().toString());
        values.put("status_id", model.getStatus_id());
        db.insert(SECOND_TABLE_NAME, null, values);
    }
}
