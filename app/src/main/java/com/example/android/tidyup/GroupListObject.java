package com.example.android.tidyup;

import android.app.Activity;
import android.widget.Button;

public class GroupListObject {
    private String groupName;
    private Button leaveButton;

    public GroupListObject(String groupName, Button leaveButton) {
        this.groupName = groupName;
        this.leaveButton = leaveButton;
    }

    public String getGroupName() {
        return groupName;
    }

    public Button getLeaveButton() {
        return leaveButton;
    }
}
