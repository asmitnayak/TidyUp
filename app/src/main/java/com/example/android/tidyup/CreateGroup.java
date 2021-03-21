package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CreateGroup extends AppCompatActivity {
    EditText groupName;
    EditText memberEmail;
    TextView inviteCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        groupName = findViewById(R.id.groupName);
    }

    public void onInvite(View view) {
        memberEmail = findViewById(R.id.memberEmail);
        //will add user by email
    }

    public void onCopy(View view) {
        //will generate and copy invite link
    }

    public void onCreateGroup(View view) {
//        takes you to group settings
    }
}