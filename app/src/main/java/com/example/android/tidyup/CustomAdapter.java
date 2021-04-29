package com.example.android.tidyup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;


public class CustomAdapter extends BaseAdapter {
    private Context context;
    //ArrayList<TaskItem> tasks;
    private Map<String, Object>tasks;
    //private Map<String, TaskItem>tasks;
    private List<String> tasksKey;
    private List tasksValues;
    //public static ArrayList<TaskItem> selectedTask = new ArrayList<TaskItem>();
    LayoutInflater inflter;
    private static final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();

    public CustomAdapter(Context applicationContext, Map<String, Object> tasks) {
        this.context = context;
        this.tasks = tasks;
        tasksKey = new ArrayList<String>(tasks.keySet());
        tasksValues = new ArrayList(tasks.values());
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
        taskNameV.setText("Task Name: " + tasksKey.get(i));
        taskNameV.setId(i + 1);
        TextView personV = (TextView) view.findViewById(R.id.taskPersonLayout);
        personV.setText("Person Assigned: " +(String)((HashMap<String, Object>)tasksValues.get(i)).get("personAssignedToTask"));

        TextView pointValueV = (TextView) view.findViewById(R.id.taskPointValueLayout);
        pointValueV.setText("Point Value: " +String.valueOf(((HashMap<String, Object>) tasksValues.get(i)).get("rewardPenaltyPointValue")));

        TextView repetitionV = (TextView) view.findViewById(R.id.taskRepetitionLayout);
        repetitionV.setText("Repetition: " +(String)((HashMap<String, Object>)tasksValues.get(i)).get("repetition"));

        TextView dateV = (TextView) view.findViewById(R.id.taskDateLayout);
        dateV.setText("Due Date: " +(String)((HashMap<String, Object>)tasksValues.get(i)).get("dateToBeCompleted"));

        CheckBox cb = (CheckBox) view.findViewById(R.id.taskSelect);
        cb.setChecked((boolean)((HashMap<String, Object>)tasksValues.get(i)).get("isChecked"));
        cb.setTag(Integer.valueOf(i));
        //view.setId(i+1);

        //cb.setOnClickListener((View.OnClickListener) view);
        //cb.setOnCheckedChangeListener(mListener); // set the listener
//
//        TextView personV = (TextView) view.findViewById(R.id.taskPersonLayout);
//        personV.setText(String.format(Locale.getDefault(),"%2f",tasks.get(i).getPersonAssignedToTask()));
//
//        TextView pointValueV = (TextView) view.findViewById(R.id.taskPointValueLayout);
//        pointValueV.setText(String.format(Locale.getDefault(),"%d",tasks.get(i).getRewardPenaltyPointValue()));
//
//        TextView repetitionV = (TextView) view.findViewById(R.id.taskRepetitionLayout);
//        repetitionV.setText(String.format(Locale.getDefault(),"%d",tasks.get(i).getRepetition()));
//
//        TextView priorityV = (TextView) view.findViewById(R.id.taskPriorityLayout);
//        priorityV.setText(String.format(Locale.getDefault(),"%d",tasks.get(i).getPriority()));
//
//        TextView dateV = (TextView) view.findViewById(R.id.taskDateLayout);
//        dateV.setText(String.format(Locale.getDefault(),"%d",tasks.get(i).getDateToBeCompleted()));
//
//        CheckBox cb = (CheckBox) view.findViewById(R.id.taskSelect);
//        cb.setChecked(tasks.get(i).getIsChecked());

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


    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    };
/*
    OnCheckedChangeListener mListener = new OnCheckedChangeListener() {

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mChecked[(Integer)buttonView.getTag()] = isChecked; // get the tag so we know the row and store the status
        }
    };
    
/*
    @Override
    public void onClick(View v) {
        CheckBox cb = (CheckBox) v.findViewById(R.id.taskSelect);
        TaskItem addTask;
        if(cb.isChecked()){
            TextView taskName = v.findViewById(R.id.taskNameLayout);
            TextView assignedUser = v.findViewById(R.id.taskPersonLayout);
            TextView taskPointVal = v.findViewById(R.id.taskPointValueLayout);
            TextView taskRepitition = v.findViewById(R.id.taskRepetitionLayout);
            TextView taskDate = v.findViewById(R.id.taskDateLayout);
            String taskNameStr = taskName.getText().toString();

            DocumentReference docRef = fFirestore.collection("task").document((String) UserManagement.getUserDetails().get("GroupID"));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> currMap = document.getData();
                    for (Map.Entry<String,Object> entry : currMap.entrySet()) {
                        if(entry.getKey().equals(taskNameStr)){
                            Object currObj = entry.getValue();
                            String currStr = currObj.toString();
                            TaskItem addTask = new TaskItem(taskName.getText().toString(), assignedUser.getText().toString(), Integer.parseInt(taskPointVal.getText().toString()),
                                    taskDate.getText().toString(), taskRepitition.getText().toString(), true);
                            Map<String, Object> delete = new HashMap<>();
                            delete.put(taskNameStr, FieldValue.delete());
                            docRef.update(delete);
                            docRef.update(taskNameStr, addTask);
                            break;
                        }
                    }

                }
            });
        }
    }

 */
}
