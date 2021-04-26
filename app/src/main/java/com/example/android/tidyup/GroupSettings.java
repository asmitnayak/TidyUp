package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class GroupSettings extends AppCompatActivity {
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private DocumentReference docRef = fFirestore.collection("Users").document(fAuth.getCurrentUser().getUid());
    boolean randomSetting;
    TextView inviteCode;
    static GroupSettingsAdapter customAdp;
    ListView membersList;
    ArrayList<String> members = new ArrayList<String>();
    String groupID;
    String code;
    Button copyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_settings);
        membersList = (ListView) findViewById(R.id.membersList);
        inviteCode = findViewById(R.id.linkText);
        code = GroupManagement.getGroupCode(groupID);
        copyBtn = (Button) findViewById(R.id.copyButton);
        copyBtn.setEnabled(false);

        if(fAuth.getCurrentUser() != null)
            groupID = GroupManagement.getGroupIDFromUserID(fAuth.getCurrentUser().getUid());
        if(groupID != null) {
            code = GroupManagement.getGroupCode(groupID);
            members = GroupManagement.getGroupMemberList(groupID);
            customAdp = new GroupSettingsAdapter(getApplicationContext(), members, groupID);
            membersList.setAdapter(customAdp);
            inviteCode.setText(code);
            copyBtn.setEnabled(true);
            randomSetting = GroupManagement.getGroupTask(groupID);
        }
        RadioButton manual = findViewById(R.id.manualSetting);
        RadioButton random = findViewById(R.id.randomSetting);
        if(!randomSetting){
            manual.setChecked(true);
            random.setChecked(false);
        }else if(randomSetting){
            manual.setChecked(false);
            random.setChecked(true);
        }

    }
    public void onCopy(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Invite Code", inviteCode.getText().toString());
        clipboard.setPrimaryClip(clip);
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.randomSetting:
                if (checked)
                    randomSetting = true;
                    GroupManagement.setGroupTask(groupID, randomSetting);
                break;
            case R.id.manualSetting:
                if (checked)
                    randomSetting = false;
                    GroupManagement.setGroupTask(groupID, randomSetting);
                break;
        }
    }

    public static void updateListView(){
        customAdp.notifyDataSetChanged();
    }
}