package com.example.android.tidyup;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddTaskToTaskPage extends TaskPage{

    private Spinner mPersonSpinner;
    private Spinner mSpinnerRewardPenaltyValue;
    private Spinner mSpinnerPriority;
    private Spinner mSpinnerRepetition;
    private static final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private static Map<String, Map<String, List<String>>> taskDB;
    private static final String COLLECTIONPATH_TASK = "Task";
    private static final String DOCUMENTPATH_TASKS = "Tasks";
    private static final DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_TASK).document(DOCUMENTPATH_TASKS);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_to_task_page);



        Spinner spinnerPerson = findViewById(R.id.personAssignedToTask);

        ArrayList<String> personList = new ArrayList<>();
        personList.add("Asmit");
        personList.add("Alvin");
        personList.add("Kao");
        personList.add("Monica");
        personList.add("Lucas");
        personList.add("Sarah");
        ArrayAdapter<String> personListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, personList);
        personListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPerson.setAdapter(personListAdapter);

        Spinner spinnerRewardPenaltyValue = findViewById(R.id.rewardPenaltyValue);
        ArrayList<String> pointList = new ArrayList<>();
        pointList.add("1");
        pointList.add("2");
        pointList.add("3");
        pointList.add("4");
        pointList.add("5");
        ArrayAdapter<String> pointListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pointList);
        pointListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRewardPenaltyValue.setAdapter(pointListAdapter);

        Spinner spinnerPriority = findViewById(R.id.taskPriorityValue);
        ArrayList<String> priorityList = new ArrayList<>();
        priorityList.add("High");
        priorityList.add("Medium");
        priorityList.add("Low");
        ArrayAdapter<String> priorityListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorityList);
        priorityListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityListAdapter);

        Spinner spinnerRepetition = findViewById(R.id.taskRepetitionValue);
        ArrayList<String> repetitionList = new ArrayList<>();
        repetitionList.add("None");
        repetitionList.add("Daily");
        repetitionList.add("Weekly");
        repetitionList.add("Monthly");
        ArrayAdapter<String> repetitionListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, repetitionList);
        repetitionListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepetition.setAdapter(repetitionListAdapter);

        mPersonSpinner = spinnerPerson;
        mSpinnerRewardPenaltyValue = spinnerRewardPenaltyValue;
        mSpinnerPriority = spinnerPriority;
        mSpinnerRepetition = spinnerRepetition;
    }

    public static int addTaskItem(String groupID, String taskName, String person, int pointValue, String priority, String dateToBeCompleted, String repetition) {
        if(taskDB.containsKey(groupID)) {
            Map<String, List<String>> groupTaskMap = new HashMap<>(taskDB.get(groupID));
            if(groupTaskMap.containsKey(taskName)){
                return 0;
            } else {
                groupTaskMap.put(taskName, Arrays.asList(new String[]{person, String.valueOf(pointValue), priority, dateToBeCompleted, repetition, null}));
                taskDB.put(groupID, groupTaskMap);
                return 1;
            }
        } else {
            Map<String, List<String>> groupTaskMap = new HashMap<>();
            groupTaskMap.put(taskName, Arrays.asList(new String[]{person, String.valueOf(pointValue), priority, dateToBeCompleted, repetition, null}));
            taskDB.put(groupID, groupTaskMap);
            return 1;
        }
        // TaskItem addTaskItem = new TaskItem(taskName, person, pointValue, priority, dateToBeCompleted, repetition);
        // taskItems.add(addTaskItem);
    }

    public static Map<String, List<String>> getGroupTaskMap(String groupID){
        Map<String, List<String>> groupTaskMap;
        if(taskDB == null){
            readGroupTaskDB();
            if(taskDB == null){
                return null;
            }
        }
        if(taskDB.containsKey(groupID)){
            groupTaskMap = new HashMap<>(taskDB.get(groupID));
        }else{
            groupTaskMap = new HashMap<>();
        }
        return groupTaskMap;
    }

    public static void readGroupTaskDB(){
        DocumentReference docRef = fFirestore.collection("Task").document("Tasks");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Tasks taskDocument = documentSnapshot.toObject(Tasks.class);
                taskDB = taskDocument.TaskMap;
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Log.w("TaskFirebase", "Error reading document", e);
            }
        });

    }

    private static class Tasks{
        public Map<String, Map<String, List<String>>> TaskMap = new HashMap<>();
        Tasks(){}
        Tasks(Map<String, Map<String, List<String>>> customMap){
            this.TaskMap = customMap;
        }
        public Map<String, Map<String, List<String>>> getRewardsMap() { return this.TaskMap;}
    }



    public void returnToTaskPage(View view) {

        // access the task name
        EditText nameIn = (EditText) findViewById(R.id.taskName);
        String name = nameIn.getText().toString();

        EditText dueDate = (EditText) findViewById(R.id.taskDueDate);
        String date = nameIn.getText().toString();

        String rewardString = mSpinnerRewardPenaltyValue.getSelectedItem().toString();
        int rewardInt = Integer.parseInt(rewardString);

        addTaskItem(groupID, name, mPersonSpinner.getSelectedItem().toString(), rewardInt,
                mSpinnerPriority.getSelectedItem().toString(), date, mSpinnerRepetition.getSelectedItem().toString());

        finish();
    }
    
}
