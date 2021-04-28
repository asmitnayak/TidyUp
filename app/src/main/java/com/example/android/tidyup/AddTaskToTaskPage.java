package com.example.android.tidyup;

import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.view.View.OnClickListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AddTaskToTaskPage extends TaskPage implements View.OnClickListener{

    private Spinner mPersonSpinner;
    private Spinner mSpinnerRewardPenaltyValue;
    private Spinner mSpinnerPriority;
    private Spinner mSpinnerRepetition;
    private static final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    /*private static Map<String, Map<String, List<String>>> taskListDB; // list of tasks
    private static Map<String, Map<String, List<String>>> taskDB; // will store the actual info of tasks
    private static final String COLLECTIONPATH_TASK = "Task";
    private static final String DOCUMENTPATH_TASKS = "Tasks";
    private static final DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_TASK).document(DOCUMENTPATH_TASKS);
*/

    Map<String, TaskItem> tasks = new HashMap<>();

    private Button addTaskButton;
    private EditText taskName;
    private EditText date;
    private static HashMap<String, Object> userMap = new HashMap<>();
    LinearLayout radioGroupLayout;
    RadioGroup rgroup;
    String userUID;
    String user;

    private List<String> membersList;
    public String getGroup(String str){
        int index = str.lastIndexOf("Group=");
        index = index + 6;
        String buildString = "";
        while(str.charAt(index) != ',' && str.charAt(index) != '}'){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    public String getName(String str){
        int index = str.lastIndexOf("Username=");
        index = index + 9;
        String buildString = "";
        while(str.charAt(index) != ',' && str.charAt(index) != '}'){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_to_task_page);

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
        radioGroupLayout = findViewById(R.id.apRewardButtonGroup);
        membersList = GroupManagement.getGroupMemberList((String) UserManagement.getUserDetails().get("GroupID"));
        rgroup = new RadioGroup(this);
        rgroup.setOrientation(RadioGroup.VERTICAL);
        RadioGroup.LayoutParams r12;
        try {
            for (int i = 0; i < membersList.size(); i++) {
                RadioButton r1 = new RadioButton(this);
                r1.setText(UserManagement.getUserNameFromUID(membersList.get(i)));
                Typeface font = Typeface.createFromAsset(getAssets(), "fonts/ArchitectsDaughter-Regular.ttf");
                r1.setTypeface(font);
                r1.setTextSize(20);
                r1.setTextColor(getApplication().getResources().getColor(R.color.fontAndButtonColor));
                r1.setId(i + 1);
                r1.setOnClickListener(this);
                r12 = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
                rgroup.addView(r1, r12);
            }
            radioGroupLayout.addView(rgroup);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Spinner spinnerRepetition = findViewById(R.id.taskRepetitionValue);
        ArrayList<String> repetitionList = new ArrayList<>();
        repetitionList.add("None");
        repetitionList.add("Daily");
        repetitionList.add("Weekly");
        repetitionList.add("Monthly");
        ArrayAdapter<String> repetitionListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, repetitionList);
        repetitionListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepetition.setAdapter(repetitionListAdapter);

       // mPersonSpinner = spinnerPerson;
        mSpinnerRewardPenaltyValue = spinnerRewardPenaltyValue;
        mSpinnerRepetition = spinnerRepetition;

/*
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if there has been a name input into the edit text box
                String string_taskName = taskName.getText().toString();
                if(string_taskName.isEmpty()){
                    Toast.makeText(AddTaskToTaskPage.this, "There is no task name.", Toast.LENGTH_SHORT).show();
                }

                String string_date = date.getText().toString();
                if(string_date.isEmpty()){
                    Toast.makeText(AddTaskToTaskPage.this, "There is no due date", Toast.LENGTH_SHORT).show();
                }

                // if both of them are populated then add the information to the hashmap
                if(!(string_taskName.isEmpty()) && !(string_date.isEmpty())){
                        Spinner personSpinner = (Spinner) findViewById(R.id.personAssignedToTask);
                        String str_person = personSpinner.getSelectedItem().toString();

                        Spinner pointValueSpinner = (Spinner) findViewById(R.id.rewardPenaltyValue);
                        String str_pointValue = pointValueSpinner.getSelectedItem().toString();
                        int int_pointValue = Integer.parseInt(str_pointValue);

                        Spinner prioritySpinner = (Spinner) findViewById(R.id.taskPriorityValue);
                        String str_priority = prioritySpinner.getSelectedItem().toString();

                        Spinner repetitionSpinner = (Spinner) findViewById(R.id.taskRepetitionValue);
                        String str_repetition = repetitionSpinner.getSelectedItem().toString();

                        TaskItem addItem = new TaskItem(string_taskName, str_person, int_pointValue,
                                str_priority, string_date, str_repetition);

                        int taskNumber = tasks.size() + 1;
                        String str_taskNumber = String.valueOf(taskNumber);

                        tasks.put(str_taskNumber, addItem);

                        finish();
                }
            }
        });
*/
    }

    /*public static int addTaskItem(String groupID, String taskName, String person, int pointValue, String priority, String dateToBeCompleted, String repetition) {
        if(taskListDB.containsKey(groupID)) {
            Map<String, List<String>> groupTaskMap = new HashMap<>(taskListDB.get(groupID));
            if(groupTaskMap.containsKey(taskName)){
                return 0;
            } else {
                groupTaskMap.put(taskName, Arrays.asList(new String[]{person, String.valueOf(pointValue), priority, dateToBeCompleted, repetition, null}));
                taskListDB.put(groupID, groupTaskMap);
                // Tasks task = new Tasks(taskDB);
                // docRef.collection(COLLECTIONPATH_TASK).document(DOCUMENTPATH_TASKS).set(task);
                // return 1;
            }
        } else {
            Map<String, List<String>> groupTaskMap = new HashMap<>();
            groupTaskMap.put(taskName, Arrays.asList(new String[]{person, String.valueOf(pointValue), priority, dateToBeCompleted, repetition, null}));
            taskListDB.put(groupID, groupTaskMap);
            // Tasks task = new Tasks(taskDB);
            // docRef.collection(COLLECTIONPATH_TASK).document(DOCUMENTPATH_TASKS).set(task);
            // return 1;
        }
        Tasks tasks = new Tasks(taskListDB);
        fFirestore.collection(COLLECTIONPATH_TASK).document(DOCUMENTPATH_TASKS).set(tasks);
        return 1;

        // TaskItem addTaskItem = new TaskItem(taskName, person, pointValue, priority, dateToBeCompleted, repetition);
        // taskItems.add(addTaskItem);
    }*/

    /*public static Map<String, List<String>> getGroupTaskMap(String groupID){
        Map<String, List<String>> groupTaskMap;
        if(taskListDB == null){
            readGroupTaskDB();
            if(taskListDB == null){
                return null;
            }
        }
        if(taskListDB.containsKey(groupID)){
            groupTaskMap = new HashMap<>(taskListDB.get(groupID));
        }else{
            groupTaskMap = new HashMap<>();
        }
        return groupTaskMap;
    }*/

   /* public static void readGroupTaskDB(){
        DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_TASK).document(DOCUMENTPATH_TASKS);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Tasks taskDocument = documentSnapshot.toObject(Tasks.class);
                taskListDB = taskDocument.TaskMap;
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
            TaskMap = new HashMap<>();
            this.TaskMap = customMap;
        }
        public Map<String, Map<String, List<String>>> getTaskMap() { return this.TaskMap;}
    }*/

    /*//@Override
    protected void onPreExecute() {
        readGroupTaskDB();
        super.onPreExecute();
    }*/



    public void returnToTaskPage(View view) {

        // access the task name

   //     EditText groupIdIn = (EditText) findViewById(R.id.taskName)
        //    String groupID = groupIdIn.getText().toString();

        EditText nameIn = (EditText) findViewById(R.id.taskName);
        String name = nameIn.getText().toString();
        if (TextUtils.isEmpty(name)){
            nameIn.setError("Please Enter a Task Name");
            return;
        }

        EditText person = (EditText) findViewById(R.id.personAssignedToTask);
        String personStr = person.getText().toString();

        EditText dueDate = (EditText) findViewById(R.id.taskDueDate);
        String date = dueDate.getText().toString();
        if (TextUtils.isEmpty(date)){
            dueDate.setError("Please Enter a Date");
            return;
        }
        if(!isValid(date)) {
            dueDate.setError("Please Enter a Date in Format ##/##");
            return;

        }

        String rewardString = mSpinnerRewardPenaltyValue.getSelectedItem().toString();
        int rewardInt = Integer.parseInt(rewardString);

        TaskManagment.addTaskItem(name, personStr, rewardInt,
                mSpinnerPriority.getSelectedItem().toString(), date, mSpinnerRepetition.getSelectedItem().toString());

        finish();
    }
    static boolean isValid(String date) {
        String regex = "\\d\\d/\\d\\d";
        return date.matches(regex);
    }
    @Override
    public void onClick(View v) {
        try {
            userUID = membersList.get((int) ((RadioButton) v).getId() - 1);
            user = UserManagement.getEmailFromUID(userUID);
        }catch (Exception e){
            Toast.makeText(AddTaskToTaskPage.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
    
}
