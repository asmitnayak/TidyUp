package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class JoinGroup extends AppCompatActivity {
    EditText joinCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

    }

    public void onJoinGroup(View view) {
        joinCode = findViewById(R.id.joinCode);
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