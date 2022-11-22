package com.example.todolist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.MainActivity;
import com.example.todolist.Model.TaskModel;
import com.example.todolist.R;
import com.example.todolist.Utils.DataBaseHelper;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<TaskModel> mList;
    private MainActivity activity;
    private DataBaseHelper myDB;

    public TaskAdapter(DataBaseHelper myDB, MainActivity activity){
        this.activity = activity;
        this.myDB = myDB;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final TaskModel item = mList.get(position);
        holder.mCheckBox.setText(item.getName());
        holder.mCheckBox.setChecked(item.getStatus_id() == 1);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChacked) {
                if(isChacked){
                    myDB.updateStatus(item.getId(), 0);
                }else{
                    myDB.updateStatus(item.getId(), 1);
                }
            }
        });
    }
    public Context getContext(){
        return activity;
    }

    public void setTasks(List<TaskModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }
    public void deleteTask(int position){
        TaskModel item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        TaskModel item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("name", item.getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox mCheckBox;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mCheckbox);
        }
    }
}
