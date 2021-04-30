package com.example.android.tidyup;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class PenaltyPopUp extends AppCompatActivity {

    private static final String EXTRA_PENALTY_NAME = "EXTRA_PENALTY_NAME";
    private static final String EXTRA_ASSIGNED_USER = "EXTRA_ASSIGNED_USER";
    private static final String EXTRA_PENALTY_DESCRIPT = "EXTRA_PENALTY_DESCRIPT";

    private TextView mPenaltyName, mAssignedUser, mPenaltyDescript;

    private String penaltyName;
    private String assignedUser;
    private String penaltyDescript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.penalty_pop_up_window);

        mPenaltyName = findViewById(R.id.ppuPenaltyName);
        mAssignedUser = findViewById(R.id.ppuUserName);
        mPenaltyDescript = findViewById(R.id.ppuPenaltyDescription);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            penaltyName = null;
            assignedUser = null;
            penaltyDescript = null;
        } else {
            penaltyName = extras.getString(EXTRA_PENALTY_NAME);
            assignedUser = extras.getString(EXTRA_ASSIGNED_USER);
            penaltyDescript = extras.getString(EXTRA_PENALTY_DESCRIPT);
        }

        try {
            mPenaltyName.setText(penaltyName);
            mAssignedUser.setText("Assigned User: " + assignedUser);
            mPenaltyDescript.setText("Penalty Description:\n"+ penaltyDescript);

        } catch (Exception e){
            Toast.makeText(PenaltyPopUp.this, "Error! " + e.getMessage(), Toast.LENGTH_LONG ).show();
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.9),(int) (height*.3));

    }

    public void deletePenalty(View view){
        PenaltyManagement.removePenalty(GroupManagement.getGroupIDFromUserID(FirebaseAuth.getInstance().getUid()), penaltyName);
        finish();
        Intent intent = RewardAndPenalty.rp.getIntent();
        RewardAndPenalty.rp.finish();
        startActivity(intent);
    }
}
