package com.example.todolist.Adapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.AddNewTask;
import com.example.todolist.MainActivity;
import com.example.todolist.Model.TaskModel;
import com.example.todolist.R;
import com.example.todolist.SplashActivity;
import com.example.todolist.Utils.DataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.MyViewHolder> {
    private List<TaskModel> mList;
    private MainActivity activity;
    private DataBaseHelper myDB;

    public TaskItemAdapter(DataBaseHelper myDB, MainActivity activity){
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
        if (item.getDate_create().after(item.getDate_end())){
            holder.calendarimage.setColorFilter(ContextCompat.getColor(getContext(), R.color.error));
            holder.mTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.error));
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        holder.mTextView.setText(formatter.format(item.getDate_end()));
        holder.mCheckBox.setText(item.getName());
        holder.mCheckBox.setChecked(item.getStatus_id() == 1);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChacked) {
                if(isChacked){
                    myDB.updateStatus(item.getId(), 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            deleteTask(position);
                        }
                    },1200);

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
        DialogInterface d = new DialogInterface() {
            @Override
            public void cancel() {

            }

            @Override
            public void dismiss() {

            }
        };
        activity.onDialogClose(d);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        TaskModel item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("name", item.getName());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager() , task.getTag());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox mCheckBox;
        ImageView calendarimage;
        TextView mTextView;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mCheckbox);
            mTextView = itemView.findViewById(R.id.date);
            calendarimage = itemView.findViewById(R.id.calendarimage);
        }
    }
}
