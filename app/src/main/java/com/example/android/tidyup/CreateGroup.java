package com.example.android.tidyup;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

import androidx.appcompat.app.AppCompatActivity;

public class CreateGroup extends AppCompatActivity {
    EditText groupName;
    EditText memberEmail;
    TextView inviteCode;
    String code;
    Button copyBtn;

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private String gID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        groupName = (EditText) findViewById(R.id.groupName);
        inviteCode = findViewById(R.id.linkText);
        code = GroupManagement.getCode();

        copyBtn = (Button) findViewById(R.id.copyButton);
        copyBtn.setEnabled(false);

        groupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    inviteCode.setText(code);
                    copyBtn.setEnabled(true);
                } else {
                    inviteCode.setText(R.string.code_goes_here);
                    copyBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void onInvite(View view) {
        if (TextUtils.isEmpty(groupName.getText().toString())){
            groupName.setError("Group Name is required");
            return;
        }
        memberEmail = findViewById(R.id.memberEmail);
        String email = memberEmail.getText().toString().trim();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, "Join my group on Tidy Up ");
        i.putExtra(Intent.EXTRA_TEXT   , "Use this join code to join my group on Tidy Up, a " +
                "platform that helps you manage chores. Here is my join code " + code + ".");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CreateGroup.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCopy(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Invite Code", inviteCode.getText().toString());
        clipboard.setPrimaryClip(clip);
    }

    public void onCreateGroup(View view) {

        if (TextUtils.isEmpty(groupName.getText().toString())){
            groupName.setError("Group Name is required");
            return;
        }
        gID = GroupManagement.getGroupID(groupName.getText().toString());
        GroupManagement.addGroupCodes(gID, code);
        GroupManagement.addUserToGroup(gID,fAuth.getCurrentUser().getUid(), inviteCode.getText().toString(), groupName.getText().toString());

        //updates user Info
        UserManagement.updateUserGroup(gID, "Admin", getApplication());
        Toast.makeText(CreateGroup.this, "You are now Admin of Group " + groupName.getText().toString(), Toast.LENGTH_LONG).show();
//
        // go to task page???
        finish();
        startActivity(new Intent(getApplicationContext(), Account.class));
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, Account.class);
        startActivity(setIntent);
        finish();
    }
}