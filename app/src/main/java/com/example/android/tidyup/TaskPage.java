package com.example.android.tidyup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class TaskPage extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    public static ArrayList<TaskItem> taskItems = new ArrayList<TaskItem>();
    CustomAdapter customAdp;
    private ImageView menu, backButton;
    private TextView pageTitle;

    ListView taskList;
    //ArrayList<String> tasks = new ArrayList<String>();
    private Map<String, Object>tasks;
    private static HashMap<String, Object> userMap = new HashMap<>();
    private CustomAdapter customAdaptor;
    private List tasksKey;
    private List tasksValues;

    private Button mAddTaskButton, mOnCompleteButton;

    //private static HashMap<String, Object> userMap = new HashMap<>();
    private static HashMap<String, Object> userTasks = new HashMap<>();
    private static final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private static TextView taskName;
    //private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    //private FirebaseFirestore taskDatabase;
    //private static Map<String, List<String>> taskMapDatabase = new HashMap<>();
    //String groupID;

    //tags for taskItem details
    private static final String COLLECTIONPATH_TASKS = "task";
//    private static final String TAG = "task";
//    private static final String KEY_TASKNAME= "taskName";
//    private static final String KEY_PERSON = "personAssignedToTask";
//    private static final String KEY_POINT= "rewardPenaltyPointValue";
//    private static final String KEY_PRIORITY= "priority";
//    private static final String KEY_REPETITION= "repetition";
//    private static final String KEY_DATE= "dateToBeCompleted";
//    private static final String KEY_CHECKED= "isChecked"; //might need to be boolean

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private  DocumentReference docRef;

    private TextView mtaskName, mtaskPerson, mtaskPoint, mtaskPriority, mtaskRepetition, mtaskDate;
    private boolean checked;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_page);

        mAddTaskButton = findViewById(R.id.addtask);
        mOnCompleteButton = findViewById(R.id.completeTask);
        taskList = (ListView) findViewById(R.id.taskList);
        //String groupID = (String) UserManagement.getUserDetails().get("GroupID");

        userMap = UserManagement.getUserDetails();
        Object userGroup1 = userMap.get("Group");
        tasks = new HashMap<>();
        tasks = TaskManagment.getDisplayMap();

/*
        readTaskDB(new FirestoreCallback() {
            @Override
            public void onCallback(Map<String, Object> taskDB) {
                tasks = taskDB;
            }
        });

 */
        String groupID = (String) userMap.get("GroupID");

        mAddTaskButton.setEnabled(!groupID.equals(""));
        mOnCompleteButton.setEnabled(!groupID.equals(""));
        if (!groupID.equals("")) // check fore resetting rewards
            RewardsManagement.resetUserRewards(groupID);

        if(!groupID.equals("")) {
            docRef = fFirestore.collection("task").document(groupID);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> taskDB = documentSnapshot.getData();
                        customAdaptor = new CustomAdapter(TaskPage.this, documentSnapshot.getData());
                        taskList.setAdapter(customAdaptor);
                        if (!groupID.equals("")) // check fore resetting rewards
                            RewardsManagement.resetUserRewards(groupID);
                    } else {
                        Toast.makeText(TaskPage.this, "Document does not Exist", Toast.LENGTH_LONG).show();
                    }
                }
            });
            docRef.addSnapshotListener(MetadataChanges.EXCLUDE, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> taskDB = documentSnapshot.getData();
                        customAdaptor = new CustomAdapter(TaskPage.this, documentSnapshot.getData());
                        taskList.setAdapter(customAdaptor);
                        if (!groupID.equals("")) // check fore resetting rewards
                            RewardsManagement.resetUserRewards(groupID);
                    } else {
                        Toast.makeText(TaskPage.this, "Document does not Exist", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        menu = findViewById(R.id.menu);
        backButton = findViewById(R.id.back_button);
        pageTitle = findViewById(R.id.pageTitle);
        pageTitle.setText("Tasks");

        mtaskName = findViewById(R.id.taskNameLayout);
        mtaskPerson = findViewById(R.id.taskPersonLayout);
        mtaskPoint = findViewById(R.id.taskPointValueLayout);
        mtaskRepetition = findViewById(R.id.taskRepetitionLayout);
        mtaskDate = findViewById(R.id.taskDateLayout);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(TaskPage.this, v);
                popup.setOnMenuItemClickListener( TaskPage.this);
                popup.inflate(R.menu.task_page_menu);
                popup.show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskPage.super.onBackPressed();
            }
        });

   }


    public static String getTaskName(String str){
        int index = str.lastIndexOf("taskName=");
        index = index + 9;
        String buildString = "";
        while(str.charAt(index) != ',' && str.charAt(index) != '}'){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    public static String getPersonStr(String str){
        int index = str.lastIndexOf("personAssignedToTask=");
        index = index + 21;
        String buildString = "";
        while(str.charAt(index) != ',' && str.charAt(index) != '}'){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    public static String getDate(String str){
        int index = str.lastIndexOf("dateToBeCompleted=");
        index = index + 18;
        String buildString = "";
        while(str.charAt(index) != ',' && str.charAt(index) != '}'){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    public static String getRepetition(String str){
        int index = str.lastIndexOf("repetition=");
        index = index + 11;
        String buildString = "";
        while(str.charAt(index) != ',' && str.charAt(index) != '}'){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    public static String getPriority(String str){
        int index = str.lastIndexOf("priority=");
        index = index + 9;
        String buildString = "";
        while(str.charAt(index) != ',' && str.charAt(index) != '}'){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    public static String getReward(String str){
        int index = str.lastIndexOf("rewardPenaltyPointValue=");
        index = index + 24;
        String buildString = "";
        while(str.charAt(index) != ',' && str.charAt(index) != '}'){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    public static String getChecked(String str){
        int index = str.lastIndexOf("isChecked=");
        index = index + 10;
        String buildString = "";
        while(str.charAt(index) != ',' && str.charAt(index) != '}'){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    public void addTask(View view){
        Intent intent = new Intent(this, AddTaskToTaskPage.class);
        startActivity(intent);
    }

    public void updateIsChecked(View view){
        CheckBox cb = (CheckBox) view.findViewById(R.id.taskSelect);

        TaskItem addTask;
        if(cb.isChecked()){
            ViewGroup viewParent = (ViewGroup) view.getParent();
            TextView taskName = (TextView) viewParent.getChildAt(0);

            String taskNameStr = taskName.getText().toString().substring(11);


            DocumentReference docRef = fFirestore.collection("task").document((String) userMap.get("GroupID"));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> currMap = document.getData();
                    for (Map.Entry<String,Object> entry : currMap.entrySet()) {
                        if(entry.getKey().equals(taskNameStr)){
                            Object currObj = entry.getValue();
                            String currStr = currObj.toString();
                            TaskItem addTask = new TaskItem(getTaskName(currStr), getPersonStr(currStr), Integer.parseInt(getReward(currStr)),
                                    getDate(currStr), getRepetition(currStr), true);
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
        else{
            ViewGroup viewParent = (ViewGroup) view.getParent();
            TextView taskName = (TextView) viewParent.getChildAt(0);

            String taskNameStr = taskName.getText().toString().substring(11);


            DocumentReference docRef = fFirestore.collection("task").document((String) userMap.get("GroupID"));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> currMap = document.getData();
                    for (Map.Entry<String,Object> entry : currMap.entrySet()) {
                        if(entry.getKey().equals(taskNameStr)){
                            Object currObj = entry.getValue();
                            String currStr = currObj.toString();
                            TaskItem addTask = new TaskItem(getTaskName(currStr), getPersonStr(currStr), Integer.parseInt(getReward(currStr)),
                                    getDate(currStr), getRepetition(currStr), false);
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

    public void completeTask(View view){
        DocumentReference docRef = fFirestore.collection("task").document((String) userMap.get("GroupID"));
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> taskDB = documentSnapshot.getData();
                for(Map.Entry<String, Object> currObj : taskDB.entrySet()){

                    if(getChecked(currObj.toString()).equals("true")){
                        UserManagement.updateUserPoints(String.valueOf(((HashMap<String, Object>) currObj.getValue()).get("rewardPenaltyPointValue")));
                        RewardsManagement.assignReward(getApplicationContext(), fAuth.getUid(),(String) userMap.get("GroupID"), Integer.parseInt((String) userMap.get("UserPoints")));
                        TaskManagment.removeTaskFromGroup((String) userMap.get("GroupID"), getTaskName(currObj.toString()));
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Log.w("TaskFirebase", "Error reading document", e);
            }
        });

        // run the TaskName.onCreate(); to refresh the page

    }

    public void setGroupTasks(String groupID) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.tpAccountPage:
                startActivity(new Intent(getApplicationContext(),Account.class));
                return true;
            case R.id.tpRewardsAndPenaltyPage:
//                finish();
                startActivity(new Intent(getApplicationContext(), RewardAndPenalty.class));
                return true;
            default:
                return false;
        }
    }
}
