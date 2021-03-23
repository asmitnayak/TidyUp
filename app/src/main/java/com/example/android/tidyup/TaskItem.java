package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

public class TaskItem {

    private String taskName;
    private String personAssignedToTask;
    private int rewardPenaltyPointValue;
    private String priority;
    private String dateToBeCompleted;
    private String repetition;

    public TaskItem(String taskName, String personAssignedToTask, int rewardPenaltyPointValue, String priority, String dateToBeCompleted, String repetition) {
        this.taskName = taskName;
        this.personAssignedToTask = personAssignedToTask;
        this.rewardPenaltyPointValue = rewardPenaltyPointValue;
        this.priority = priority;
        this.dateToBeCompleted = dateToBeCompleted;
        this.repetition = repetition;
    }





}
