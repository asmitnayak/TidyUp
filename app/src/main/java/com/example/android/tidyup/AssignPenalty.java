package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssignPenalty extends AppCompatActivity {
    EditText penaltyNameEDT;
    String penaltyName;
    EditText penaltyReasonEDT;
    String penaltyReason;
    EditText offendingEDT;
    String offendingUser;
    String groupID;
    Map<String, List<Object>> penaltyMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_penalty);
        this.penaltyNameEDT = findViewById(R.id.penaltyName);
        this.penaltyReasonEDT = findViewById(R.id.reason);
        this.offendingEDT = findViewById(R.id.user);
    }

    public void checkInput(){
        if (TextUtils.isEmpty(penaltyName)){
            penaltyNameEDT.setError("Please Enter a Penalty Name");
            return;
        }
        if (TextUtils.isEmpty(penaltyReason)){
            penaltyReasonEDT.setError("Please Enter a Reason");
            return;
        }
        if (TextUtils.isEmpty(offendingUser)){
            offendingEDT.setError("Please Enter A User");
            return;
        }
        groupID = (String) UserManagement.getUserDetails().get("GroupID");
        if(groupID != null) {
            ArrayList<String> members = GroupManagement.getGroupMemberList(groupID);
            penaltyMap = PenaltyManagement.getGroupPenaltyMap(groupID);
            if(penaltyMap != null) {
                ArrayList<String> nameList = PenaltyManagement.getPenaltyNameList(penaltyMap);
                if(nameList != null) {
                    if (!nameList.contains(penaltyName)) {
                        penaltyNameEDT.setError("Please Enter a Existing Penalty Name");
                        return;
                    }
                }
            }
            if(members != null){
                ArrayList<String> membersList = new ArrayList<String>();
                for(String name : members){
                    membersList.add(UserManagement.getUserNameFromUID(name));
                }
                if(!membersList.contains(offendingUser)){
                    offendingEDT.setError("Please Enter a Existing User");
                    return;
                }
            }
        }
    }
    public void onAssignPenalty(View view) {
        this.penaltyName = this.penaltyNameEDT.getText().toString();
        this.penaltyReason = this.penaltyReasonEDT.getText().toString();
        this.offendingUser = this.offendingEDT.getText().toString();
        checkInput();
        Intent intent = new Intent(this, RewardAndPenalty.class);
        startActivity(intent);


    }
}