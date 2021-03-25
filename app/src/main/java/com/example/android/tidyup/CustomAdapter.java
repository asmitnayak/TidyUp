package com.example.android.tidyup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;


public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<TaskItem> tasks;
    public static ArrayList<TaskItem> selectedTask = new ArrayList<TaskItem>();
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, ArrayList<TaskItem> food) {
        this.context = context;
        this.tasks = tasks;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        if(tasks == null){
            return 0;
        }

        return tasks.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.task_page_layout, null);

        TextView taskNameV = (TextView) view.findViewById(R.id.taskNameLayout);
        taskNameV.setText(tasks.get(i).getTaskName());

        TextView personV = (TextView) view.findViewById(R.id.taskPersonLayout);
        personV.setText(String.format(Locale.getDefault(),"%2f",tasks.get(i).getPersonAssignedToTask()));

        TextView pointValueV = (TextView) view.findViewById(R.id.taskPointValueLayout);
        pointValueV.setText(String.format(Locale.getDefault(),"%d",tasks.get(i).getRewardPenaltyPointValue()));

        TextView repetitionV = (TextView) view.findViewById(R.id.taskRepetitionLayout);
        repetitionV.setText(String.format(Locale.getDefault(),"%d",tasks.get(i).getRepetition()));

        TextView priorityV = (TextView) view.findViewById(R.id.taskPriorityLayout);
        priorityV.setText(String.format(Locale.getDefault(),"%d",tasks.get(i).getPriority()));

        TextView dateV = (TextView) view.findViewById(R.id.taskDateLayout);
        dateV.setText(String.format(Locale.getDefault(),"%d",tasks.get(i).getDateToBeCompleted()));

        CheckBox cb = (CheckBox) view.findViewById(R.id.taskSelect);
        cb.setChecked(tasks.get(i).getIsChecked());

/*
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    q.setText("1");
                    tasks.get(i).setItemChoosen(1);
                    tasks.get(i).setIsChecked(true);
                    selectedFood.add(tasks.get(i));
                }else{
                    q.setText("0");
                    tasks.get(i).setItemChoosen(0);
                    tasks.get(i).setIsChecked(false);
                    selectedFood.remove(tasks.get(i));
                }
            }
        });
    */
        return view;
    }
}
