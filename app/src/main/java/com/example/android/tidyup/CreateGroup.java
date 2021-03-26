package com.example.android.tidyup;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class CreateGroup extends AppCompatActivity {
    EditText groupName;
    EditText memberEmail;
    TextView inviteCode;
    String code;

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        groupName = findViewById(R.id.groupName);
        inviteCode = findViewById(R.id.linkText);
        code = GroupManagement.getCode();
        inviteCode.setText(code);

    }
    public void onInvite(View view) {
        memberEmail = findViewById(R.id.memberEmail);
        //will add user by email
    }

    public void onCopy(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Invite Code", code);
        clipboard.setPrimaryClip(clip);
    }

    public void onCreateGroup(View view) {

        if (TextUtils.isEmpty(groupName.getText().toString())){
            groupName.setError("Group Name is required");
            return;
        }

        GroupManagement.addUserToGroup(GroupManagement.getGroupID(groupName.getText().toString()),fAuth.getUid(), inviteCode.getText().toString(), groupName.getText().toString());
        // go to task page???
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, Account.class);
        startActivity(setIntent);
        finish();
    }
}