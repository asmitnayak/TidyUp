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
    public static Map<String, Object>tasks;
    //private Map<String, TaskItem>tasks;
    private List<String> tasksKey;
    public static List tasksValues;
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

    public static Map<String, Object> getTasks(){
        return tasks;
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
//        taskNameV.setId(i + 1);
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


        return view;
    }


    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    };


}
