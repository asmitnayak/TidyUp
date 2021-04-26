package com.example.android.tidyup;

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
import java.util.List;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



public class TaskPage extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    public static ArrayList<TaskItem> taskItems = new ArrayList<TaskItem>();
    CustomAdapter customAdp;
    private ImageView menu;
    private TextView pageTitle;

    ListView taskList;
    String[] tasks = new String[1];
    //private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    //private FirebaseFirestore taskDatabase;
    //private static Map<String, List<String>> taskMapDatabase = new HashMap<>();
    //String groupID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_page);

        menu = findViewById(R.id.menu);
        pageTitle = findViewById(R.id.pageTitle);
        pageTitle.setText("Tasks");

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(TaskPage.this, v);
                popup.setOnMenuItemClickListener( TaskPage.this);
                popup.inflate(R.menu.task_page_menu);
                popup.show();
            }
        });

        //taskList = findViewById(R.id.taskList);
        tasks[0] = "Test Task 1";

        ArrayAdapter<String> tasksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasks);
        //if (tasksAdapter != null )
            //taskList.setAdapter(tasksAdapter);
        //action bar

    }


    //taskDatabase = FirebaseFirestore.getInstance();
    //if(fAuth.getCurrentUser() != null)
    //  groupID = GroupManagement.getGroupIDFromUserID(fAuth.getUid());
    //try {
    //foodItems = m.read_menu();
    //} catch (IOException e) {
    //    e.printStackTrace();
    //}
/*
        taskList = (ListView) findViewById(R.id.taskList);

        customAdp = new CustomAdapter(getApplicationContext(), taskItems);
        taskList.setAdapter(customAdp);
  */
    // when a task is selected and the completeTask button is pressed
    // the task will be removed from the task page
   /* public void completeTask(View view){
        TaskManagement.removeTaskFromGroup(groupID, taskName);

    }*/

    public void addTask(View view){
        Intent intent = new Intent(this, AddTaskToTaskPage.class);
        startActivity(intent);
    }

    // public void getGroupTasks(String groupID) {
        // if(taskDatabase == null)
         //    return;
   //  }

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
            case R.id.acRewardsAndPenaltyPage:
//                finish();
                startActivity(new Intent(getApplicationContext(), RewardAndPenalty.class));
                return true;
            default:
                return false;
        }
    }
}
