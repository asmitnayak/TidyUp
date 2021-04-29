package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

public class TaskItem {

    private String taskName;
    private String personAssignedToTask;
    private int rewardPenaltyPointValue;
    //private String ;
    private String dateToBeCompleted;
    private String repetition;

    private boolean isChecked;

    public TaskItem(String taskName, String personAssignedToTask, int rewardPenaltyPointValue,  String dateToBeCompleted, String repetition) {
        this.taskName = taskName;
        this.personAssignedToTask = personAssignedToTask;
        this.rewardPenaltyPointValue = rewardPenaltyPointValue;

        this.dateToBeCompleted = dateToBeCompleted;
        this.repetition = repetition;

        this.isChecked = false;
    }

    public void setTaskName(String taskName){ this.taskName = taskName; }
    public void setPersonAssignedToTask(String personAssignedToTask){ this.personAssignedToTask = personAssignedToTask; }
    public void setRewardPenaltyPointValue(int rewardPenaltyPointValue){ this.rewardPenaltyPointValue = rewardPenaltyPointValue; }

    public void setDateToBeCompleted(String dateToBeCompleted){ this.dateToBeCompleted = dateToBeCompleted; }
    public void setRepetition(String repetition){ this.repetition = repetition; }

    public String getTaskName(){ return this.taskName; }
    public String getPersonAssignedToTask(){ return this.personAssignedToTask; }
    public int getRewardPenaltyPointValue(){ return this.rewardPenaltyPointValue;}
    public String getDateToBeCompleted(){ return this.dateToBeCompleted; }
    public String getRepetition(){ return this.repetition; }


    public void setIsChecked(boolean select){ this.isChecked = select; }
    public boolean getIsChecked() {
        return isChecked;
    }


}
