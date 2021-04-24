package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddPenalty extends AppCompatActivity {
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    EditText penaltyNameEDT;
    String penaltyName;
    EditText penaltyDescriptionEDT;
    String penaltyDescription;
    ArrayList<Object> penaltiesValue;
    int listItem;
    private Map<String, List<Object>> penaltyMap;
    ArrayList<String>penaltyKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_penalty);
        PenaltyManagement pm = new PenaltyManagement();
        pm.execute();
        this.penaltyNameEDT = findViewById(R.id.penaltyName);
        this.penaltyDescriptionEDT = findViewById(R.id.penaltyDescription);
        //When a penalty is clicked load penalty info into addPenalty window
        //This will allow the user to edit the penalty.
        Intent intent = getIntent();
        listItem = intent.getIntExtra("listItem", -1);
        if(listItem != -1){
            String groupID = GroupManagement.getGroupIDFromUserID(fAuth.getCurrentUser().getUid());
            //Load in penalty data
            penaltyMap = PenaltyManagement.getGroupPenaltyMap(groupID);
            penaltyKey = PenaltyManagement.getPenaltyNameList(penaltyMap);
            String penaltyName = penaltyKey.get(listItem);
            if (penaltyName != null) {
                penaltyNameEDT.setText(penaltyName);
                penaltiesValue = PenaltyManagement.getPenaltyInfo(groupID, penaltyName);;
                penaltyDescriptionEDT.setText(penaltiesValue.get(0).toString());
            }
        }

    }

    public void onAddPenalty(View view) {
        this.penaltyDescription = this.penaltyDescriptionEDT.getText().toString();
        this.penaltyName = this.penaltyNameEDT.getText().toString();
        PenaltyManagement.addPenalty(GroupManagement.getGroupIDFromUserID(fAuth.getCurrentUser().getUid()),
                this.penaltyDescription, this.penaltyName);
        Intent intent = new Intent(this, RewardAndPenalty.class);
        finish();
        startActivity(intent);
    }
}