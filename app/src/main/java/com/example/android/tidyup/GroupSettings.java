package com.example.android.tidyup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;


public class GroupSettings extends AppCompatActivity {
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private DocumentReference docRef = fFirestore.collection("Users").document(fAuth.getUid());
    boolean randomSetting;
    GroupSettingsAdapter customAdp;
    ListView membersList;
    ArrayList<String> members = new ArrayList<String>();
    String groupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_settings);
        membersList = (ListView) findViewById(R.id.membersList);
        if(fAuth.getCurrentUser() != null){
            groupID = "";
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    groupID = documentSnapshot.getString("GroupID");
                }else {
                    return;
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("GroupSettings", "Error reading document", e);
            }
        });
        }
        members = GroupManagement.getGroupMemberList(groupID);

        customAdp = new GroupSettingsAdapter(getApplicationContext(), members, groupID);
        membersList.setAdapter(customAdp);
        randomSetting = GroupManagement.getGroupTask(groupID);
        RadioButton manual = findViewById(R.id.manualSetting);
        RadioButton random = findViewById(R.id.randomSetting);
        if(randomSetting == false){
            manual.setChecked(true);
            random.setChecked(false);
        }else if(randomSetting == true){
            manual.setChecked(false);
            random.setChecked(true);
        }

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
}