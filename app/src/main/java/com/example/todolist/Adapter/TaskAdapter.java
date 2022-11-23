package com.example.todolist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.AddNewTask;
import com.example.todolist.MainActivity;
import com.example.todolist.Model.GroupedTasks;
import com.example.todolist.Model.TaskModel;
import com.example.todolist.R;
import com.example.todolist.SplashActivity;
import com.example.todolist.Utils.DataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<GroupedTasks> mList;
    private List<TaskModel> tasks;
    private MainActivity activity;
    private DataBaseHelper myDB;
    private TaskItemAdapter adapter;
    private boolean expanded;

    public TaskAdapter(DataBaseHelper myDB, MainActivity activity){
        this.activity = activity;
        this.myDB = myDB;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grouped_tasks, parent, false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        expanded = false;
        holder.mrecyclerView.setVisibility(View.GONE);
        final GroupedTasks item = mList.get(position);
        tasks = new ArrayList<>();
        adapter = new TaskItemAdapter(myDB , activity);
        holder.mrecyclerView.setHasFixedSize(true);
        holder.mrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        holder.mrecyclerView.setAdapter(adapter);
        tasks = item.getTasks();
        Collections.reverse(tasks);
        adapter.setTasks(tasks);


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        holder.groupname.setText(item.getGroup_name());
        System.out.println(item.getTasks().size());
        String s = "0";



        holder.groupname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expanded == true){
                    expanded = false;
                    holder.mrecyclerView.setVisibility(View.GONE);
                }else{
                    expanded = true;
                    holder.mrecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public Context getContext(){
        return activity;
    }

    public void setTasks(List<GroupedTasks> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void setExpand(){

    }
//    public void deleteTask(int position){
//        TaskModel item = mList.get(position);
//        myDB.deleteTask(item.getId());
//        mList.remove(position);
//        notifyItemRemoved(position);
//    }

//    public void editItem(int position){
//        TaskModel item = mList.get(position);
//
//        Bundle bundle = new Bundle();
//        bundle.putInt("id", item.getId());
//        bundle.putString("name", item.getName());
//
//        AddNewTask task = new AddNewTask();
//        task.setArguments(bundle);
//        task.show(activity.getSupportFragmentManager() , task.getTag());
//    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
//        CheckBox mCheckBox;
//        TextView mTextView;
        private RecyclerView mrecyclerView;
        private Button groupsize;
        private TextView groupname;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            mrecyclerView = itemView.findViewById(R.id.groupview);
            groupsize = itemView.findViewById(R.id.groupsize);
            groupname = itemView.findViewById(R.id.grouptext);

        }
    }
}
