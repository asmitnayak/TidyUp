package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssignPenalty extends AppCompatActivity implements View.OnClickListener{
    EditText penaltyNameEDT;
    String penaltyName;
    EditText penaltyReasonEDT;
    String penaltyReason;
    //EditText offendingEDT;
    String offendingUser;
    String groupID;
    private String offendingUserUID;
    LinearLayout radioGroupLayout;
    RadioGroup rgroup;
    Map<String, List<Object>> penaltyMap;
    private List<String> membersList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_penalty);
        this.penaltyNameEDT = findViewById(R.id.penaltyName);
        this.penaltyReasonEDT = findViewById(R.id.reason);
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
            //offendingEDT.setError("Please Enter A User Email");
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
        }

    }
    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    public void onAssignPenalty(View view) {
        this.penaltyName = this.penaltyNameEDT.getText().toString().toLowerCase();
        this.penaltyReason = this.penaltyReasonEDT.getText().toString().toLowerCase();
        //this.offendingUser = this.offendingEDT.getText().toString().toLowerCase();
        checkInput();
        if(isValid(offendingUser)) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{offendingUser});
            i.putExtra(Intent.EXTRA_SUBJECT, "You have been assigned a penalty ");
            i.putExtra(Intent.EXTRA_TEXT, "Your penalty is " + penaltyName +
                    ". The reason you are receiving this penalty is " + penaltyReason);
            try {
                switch (PenaltyManagement.assignPenalty(groupID, penaltyName, offendingUserUID)){
                    case -1:
                        Toast.makeText(AssignPenalty.this, "No Penalty Database found", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(AssignPenalty.this, "No Penalty Database found for current Group named" + GroupManagement.getGroupName(groupID), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(AssignPenalty.this, "Successfully assigned penalty" + penaltyName + " to user " + UserManagement.getUserNameFromUID(offendingUserUID), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(AssignPenalty.this, "No PenaltyName found with name " + penaltyName, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(AssignPenalty.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(AssignPenalty.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        Intent intent = new Intent(this, RewardAndPenalty.class);
        startActivity(intent);


    }

    @Override
    public void onClick(View v) {
        try {
            offendingUserUID = membersList.get((int) ((RadioButton) v).getId() - 1);
            offendingUser = UserManagement.getEmailFromUID(offendingUserUID);
        }catch (Exception e){
            Toast.makeText(AssignPenalty.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}