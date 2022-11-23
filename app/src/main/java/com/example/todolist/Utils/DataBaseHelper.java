package com.example.todolist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todolist.Model.GroupModel;
import com.example.todolist.Model.GroupedTasks;
import com.example.todolist.Model.TaskModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DataBaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "TODO_DATABASE";

    private static final String TABLE_NAME = "Groups";
    private static final String SECOND_TABLE_NAME = "Task";
    private static final String THIRD_TABLE_NAME = "Status";
private static final String COL_1 = "Status";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 3);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SECOND_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + THIRD_TABLE_NAME);
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


        db.execSQL("INSERT INTO " + TABLE_NAME+ " values (1,'Ожидает выполнения')");
        db.execSQL("INSERT INTO " + THIRD_TABLE_NAME + " VALUES (1,'Ожидает выполнения')");
        db.execSQL("INSERT INTO " + THIRD_TABLE_NAME + " VALUES (2,'Просрочено')");
        db.execSQL("INSERT INTO " + THIRD_TABLE_NAME + " VALUES (3,'Выполнено')");
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
        List<GroupedTasks> tasks = getGroupedTasks();
    }
     public void updateTask(int id, String task){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", task);
        db.update(SECOND_TABLE_NAME, values, "id+?", new String[]{String.valueOf(id)});
     }

    public void updateGroup(int id, String name){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        db.update(TABLE_NAME, values, "id+?", new String[]{String.valueOf(id)});
    }
     public void updateStatus(int id, int status){
         db = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put("status_id", status);
         db.update(SECOND_TABLE_NAME, values, "id=?", new String[]{String.valueOf(id)});
     }

     public void deleteTask(int id){
        db = this.getWritableDatabase();
        db.delete(SECOND_TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
     }

    public void deleteGroup(int id){
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
    }

    public List<TaskModel> getAllTasks(){
        db = this.getWritableDatabase();
        Cursor cursor = null;

        List<TaskModel> modelList = new ArrayList<>();
        db.beginTransaction();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        try{
            cursor = db.query(SECOND_TABLE_NAME, null, null, null,null,null,null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        TaskModel task = new TaskModel();
                        task.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                        task.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                        task.setGroup_id(cursor.getInt(cursor.getColumnIndexOrThrow("group_id")));
                        task.setDate_create(formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow("date_create"))));
                        modelList.add(task);
                    }while(cursor.moveToNext());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }

    public List<TaskModel> getAllTasksByGroupId(int id){
        db = this.getWritableDatabase();
        Cursor cursor = null;

        List<TaskModel> modelList = new ArrayList<>();
        db.beginTransaction();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        try{
            cursor = db.rawQuery("SELECT * FROM " + SECOND_TABLE_NAME + " WHERE group_id = " + id,null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        TaskModel task = new TaskModel();
                        task.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                        task.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                        task.setGroup_id(cursor.getInt(cursor.getColumnIndexOrThrow("group_id")));
                        task.setDate_create(formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow("date_create"))));
                        modelList.add(task);
                    }while(cursor.moveToNext());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }

    public List<GroupModel> getAllGroups(){
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<GroupModel> modelList = new ArrayList<>();
        try{
            cursor = db.rawQuery("SELECT* FROM " + TABLE_NAME,null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        GroupModel group = new GroupModel();
                        group.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                        group.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                        modelList.add(group);
                    }while(cursor.moveToNext());
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return modelList;
    }

    public List<GroupedTasks> getGroupedTasks(){
        List<GroupedTasks> groupedTasks = new ArrayList<>();
        List<GroupModel> groups = getAllGroups();
        for (GroupModel item: groups) {
            GroupedTasks groupedTask = new GroupedTasks();
            List<TaskModel> tasks = getAllTasksByGroupId(item.getId());
            groupedTask.setTasks(tasks);
            groupedTask.setGroup_id(item.getId());
            groupedTasks.add(groupedTask);
        }
        for (GroupedTasks g: groupedTasks) {
            System.out.println(g.getTasks());
        }
        return groupedTasks;
    }
}
