package com.example.android.tidyup;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.Spinner;

import java.util.ArrayList;

public class AddTaskToTaskPage extends TaskPage{

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
        repetitionList.add("Daily");
        repetitionList.add("Weekly");
        repetitionList.add("Monthly");
        ArrayAdapter<String> repetitionListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, repetitionList);
        repetitionListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepetition.setAdapter(repetitionListAdapter);
    }

    public void addTaskItem(String taskName, String person, int pointValue, String priority, String dateToBeCompleted, String repetition) {
        TaskItem addTaskItem = new TaskItem(taskName, person, pointValue, priority, dateToBeCompleted, repetition);
        taskItems.add(addTaskItem);
    }


    public void returnToTaskPage(View view) {
        System.out.println("\n\n running return to admin page \n");
        EditText nameIn = (EditText) findViewById(R.id.taskName);
        String name = nameIn.getText().toString();

        //EditText personAssigned = (EditText) findViewById(R.id.personAssignedToTask);
        //String person = personAssigned.getText().toString();

        EditText dueDate = (EditText) findViewById(R.id.taskDueDate);
        String date = nameIn.getText().toString();

        //EditText rewardPenaltyPointValue = (EditText) findViewById(R.id.rewardPenaltyValue);
        //String pointValue = rewardPenaltyPointValue.getText().toString();
        //int value = Integer.parseInt(pointValue);

        //EditText inStockIn = (EditText) findViewById(R.id.menuAvailability);
        //String inStockString = inStockIn.getText().toString();
        //int inStock = Integer.parseInt(inStockString);

        //addTaskItem(name, price, inStock);

        finish();
    }
    
}
