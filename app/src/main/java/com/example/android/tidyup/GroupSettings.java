package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;


public class GroupSettings extends AppCompatActivity {
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    //private DocumentReference docRef = fFirestore.collection("Groups").document(fAuth.getUid());
    boolean randomSetting;
    GroupSettingsAdapter customAdp;
    ListView membersList;
    ArrayList<String> listOfMembers = new ArrayList<String>();
    String groupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_settings);
        membersList = (ListView) findViewById(R.id.membersList);
//        if(fAuth.getCurrentUser() != null){
//            //String groupID = fAuth.getCurrentUser().get("Group ID");
//        }
//        ArrayList<String> members = GroupManagement.getGroupMemberList(groupID);
        listOfMembers.add("Max");
        listOfMembers.add("Rachel");
        listOfMembers.add("Mark");

        customAdp = new GroupSettingsAdapter(getApplicationContext(), listOfMembers);
        membersList.setAdapter(customAdp);
        //randomSetting = get from database
        //members = get from database based off of Users id?

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.randomSetting:
                if (checked)
                    randomSetting = true;
                    //add setting to group database
                break;
            case R.id.manualSetting:
                if (checked)
                    randomSetting = false;
                    //add setting to group database
                break;
        }
    }
}