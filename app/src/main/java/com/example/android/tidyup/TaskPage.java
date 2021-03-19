package com.example.android.tidyup;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TaskPage extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_page);
    }

    public static ArrayList<TaskItem> taskItems = new ArrayList<TaskItem>();

}
