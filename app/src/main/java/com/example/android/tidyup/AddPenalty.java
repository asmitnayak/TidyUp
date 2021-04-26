package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

    }

    public void onAddPenalty(View view) {
        this.penaltyDescription = this.penaltyDescriptionEDT.getText().toString();
        this.penaltyName = this.penaltyNameEDT.getText().toString();
        if (TextUtils.isEmpty(penaltyName)){
            penaltyNameEDT.setError("Please Enter a Name");
            return;
        }
        if (TextUtils.isEmpty(penaltyDescription)){
            penaltyDescriptionEDT.setError("Please Enter a Description");
            return;
        }

        PenaltyManagement.addPenalty(GroupManagement.getGroupIDFromUserID(fAuth.getCurrentUser().getUid()),
                this.penaltyDescription, this.penaltyName);
        Intent intent = new Intent(this, RewardAndPenalty.class);
        finish();
        startActivity(intent);
    }
}