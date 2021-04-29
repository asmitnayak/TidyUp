package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class JoinGroup extends AppCompatActivity {
    EditText joinCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
    }

    public void onJoinGroup(View view) {
        joinCode = findViewById(R.id.joinCode);
        String grp = GroupManagement.getGroup(joinCode.getText().toString()); // groupID
        if (TextUtils.isEmpty(joinCode.getText().toString())){
            joinCode.setError("Please Enter a Code");
            return;
        }
        if (grp == null){
            joinCode.setError("Enter a Valid Join Code");
            return;
        }

        UserManagement.updateUserGroup(grp, "User", getApplicationContext());
        GroupManagement.addUserToGroup(grp, FirebaseAuth.getInstance().getUid(), joinCode.getText().toString(), GroupManagement.getGroupName(grp));
        finish();
        startActivity(new Intent(this, TaskPage.class));
        //if(join code exists in database)
        //  add user to group

    }

    public void loadGroupInfo(){
        //There is going to need to be certain variables that are default and are changed at this state
        //Individual todo list, group todo list, group rewards list, group penalties list, will all need to be
        //reassigned at this point
        //Will need to call onCreate again possibly on the main page not sure though
    }
}