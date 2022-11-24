package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todolist.Adapter.TaskAdapter;
import com.example.todolist.Model.GroupedTasks;
import com.example.todolist.Model.TaskModel;
import com.example.todolist.Utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener {

    private RecyclerView mrecyclerView;
    private FloatingActionButton mfab;
    private DataBaseHelper myDB;
    private List<GroupedTasks> mList;
    private TaskAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mrecyclerView = findViewById(R.id.resyclerview);
        mfab = findViewById(R.id.floatingActionButton);
        myDB = new DataBaseHelper(MainActivity.this);

        mList = new ArrayList<>();
        adapter = new TaskAdapter(myDB , MainActivity.this);

        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrecyclerView.setAdapter(adapter);
        mList = myDB.getGroupedTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);

        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewGroup.newInstance().show(getSupportFragmentManager() , AddNewGroup.TAG);

            }
        });

    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getGroupedTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();
    }
}