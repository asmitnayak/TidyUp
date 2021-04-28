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
import android.widget.ArrayAdapter;
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
    private Map<String, Map<String, Object>>tasks;
    private static HashMap<String, Object> userMap = new HashMap<>();
    private CustomAdapter customAdaptor;
    private List tasksKey;
    private List tasksValues;

    //private static HashMap<String, Object> userMap = new HashMap<>();
    private static HashMap<String, Object> userTasks = new HashMap<>();
    private static final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();

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
    private final DocumentReference docRef  = fFirestore.collection(COLLECTIONPATH_TASKS).document(fAuth.getCurrentUser().getUid());

    private TextView mtaskName, mtaskPerson, mtaskPoint, mtaskPriority, mtaskRepetition, mtaskDate;
    private boolean checked;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_page);

        taskList = (ListView) findViewById(R.id.taskList);
        //String groupID = (String) UserManagement.getUserDetails().get("GroupID");
        userMap = UserManagement.getUserDetails();
        Object userGroup1 = userMap.get("Group");
        tasks = new HashMap<>();
        tasks = TaskManagment.getGroupTaskMap(userGroup1.toString());
        customAdaptor = new CustomAdapter(this, tasks);
        taskList.setAdapter(customAdaptor);
        tasksKey = new ArrayList<String>(tasks.keySet());
        tasksValues = new ArrayList(tasks.values());

        menu = findViewById(R.id.menu);
        backButton = findViewById(R.id.back_button);
        pageTitle = findViewById(R.id.pageTitle);
        pageTitle.setText("Tasks");

        mtaskName = findViewById(R.id.taskNameLayout);
        mtaskPerson = findViewById(R.id.taskPersonLayout);
        mtaskPoint = findViewById(R.id.taskPointValueLayout);
        mtaskPriority = findViewById(R.id.taskPriorityLayout);
        mtaskRepetition = findViewById(R.id.taskRepetitionLayout);
        mtaskDate = findViewById(R.id.taskDateLayout);

//// load and display user info on Task Page
//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()){
//                    String name = documentSnapshot.getString(KEY_TASKNAME);
//                    String person = documentSnapshot.getString(KEY_PERSON);
//                    String point = documentSnapshot.getString(KEY_POINT);
//                    String priority = documentSnapshot.getString(KEY_PRIORITY);
//                    String repetition = documentSnapshot.getString(KEY_REPETITION);
//                    String date = documentSnapshot.getString(KEY_DATE);
//
//                    mtaskName.setText("Task Name: " + name);
//                    mtaskPerson.setText("Person Assigned: " + person);
//                    mtaskPoint.setText("Point Value: " + point);
//                    mtaskPriority.setText("Priority Level: " + priority);
//                    mtaskRepetition.setText("Repetition: " + repetition);
//                    mtaskDate.setText("Due Date: " + date);
//
//                }else {
//                    Toast.makeText(TaskPage.this, "Document does not Exist", Toast.LENGTH_LONG).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(TaskPage.this, "Error! "+ e.getMessage(), Toast.LENGTH_LONG).show();
//                Log.d(TAG, e.toString());
//            }
//        });
//
//        docRef.addSnapshotListener(MetadataChanges.EXCLUDE, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.w(TAG, "listen:error", error);
//                    return;
//                }
//
//                if (documentSnapshot.exists()){
//                    String name = documentSnapshot.getString(KEY_TASKNAME);
//                    String person = documentSnapshot.getString(KEY_PERSON);
//                    String point = documentSnapshot.getString(KEY_POINT);
//                    String priority = documentSnapshot.getString(KEY_PRIORITY);
//                    String repetition = documentSnapshot.getString(KEY_REPETITION);
//                    String date = documentSnapshot.getString(KEY_DATE);
//
//
//
//                }else {
//                    Toast.makeText(TaskPage.this, "Document does not Exist", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

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

        userMap = UserManagement.getUserDetails();
        Object userGroup = userMap.get("Group");

//        fFirestore.collection("task").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Map<String, Object> currTaskMap = document.getData();
//                            for (Map.Entry<String,Object> entry : currTaskMap.entrySet()) {
//                                Object currTask = entry.getValue();
//                                String taskName = getTaskName(currTask.toString());
//                                String getDate = getDate(currTask.toString());
//                                String person = getPersonStr(currTask.toString());
//                                String repetition = getRepetition(currTask.toString());
//                                String priority = getPriority(currTask.toString());
//                                String reward = getReward(currTask.toString());
//                                String checked = getChecked(currTask.toString());
//
//
//                                mtaskName.setText("Task Name: " + taskName);
//                                mtaskPerson.setText("Person Assigned: " + person);
//                                mtaskPoint.setText("Point Value: " + reward);
//                                mtaskPriority.setText("Priority Level: " + priority);
//                                mtaskRepetition.setText("Repetition: " + repetition);
//                                mtaskDate.setText("Due Date: " + getDate);
//                                /*
//                                System.out.println("taskName   : " + taskName);
//                                System.out.println("getDate    : " + getDate);
//                                System.out.println("person     : " + person);
//                                System.out.println("repetition : " + repetition);
//                                System.out.println("priority   : " + priority);
//                                System.out.println("reward     : " + reward);
//                                System.out.println("checked    : " + checked);*/
//                            }
//
//                        }
//                    }
//                });
//
//
//       // DocumentReference docRef = fFirestore.collection("task").document(userGroup.toString());
//        //docRef.update(tasks);
//
//       // ArrayAdapter<String> tasksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasks);
//        //if (tasksAdapter != null )
//            //taskList.setAdapter(tasksAdapter);
//        //action bar
//
   }
    public String getTaskName(String str){
        int index = str.lastIndexOf("taskName=");
        index = index + 9;
        String buildString = "";
        while(str.charAt(index) != ','){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    public String getPersonStr(String str){
        int index = str.lastIndexOf("personAssignedToTask=");
        index = index + 21;
        String buildString = "";
        while(str.charAt(index) != ','){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    public String getDate(String str){
        int index = str.lastIndexOf("dateToBeCompleted=");
        index = index + 18;
        String buildString = "";
        while(str.charAt(index) != ','){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    public String getRepetition(String str){
        int index = str.lastIndexOf("repetition=");
        index = index + 11;
        String buildString = "";
        while(str.charAt(index) != ','){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    public String getPriority(String str){
        int index = str.lastIndexOf("priority=");
        index = index + 9;
        String buildString = "";
        while(str.charAt(index) != ','){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    public String getReward(String str){
        int index = str.lastIndexOf("rewardPenaltyPointValue=");
        index = index + 24;
        String buildString = "";
        while(str.charAt(index) != ',' && str.charAt(index) != '}'){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    public String getChecked(String str){
        int index = str.lastIndexOf("isChecked=");
        index = index + 10;
        String buildString = "";
        while(str.charAt(index) != '}'){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    public void addTask(View view){
        Intent intent = new Intent(this, AddTaskToTaskPage.class);
        startActivity(intent);
    }

    public void completeTask(View view){

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
