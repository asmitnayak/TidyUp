package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RewardAssignmentPopUp extends AppCompatActivity {
    private static final String EXTRA_REWARD_NAME = "EXTRA_REWARD_NAME";
    private static final String EXTRA_REWARD_DESCRIPT = "EXTRA_REWARD_DESCRIPT";
    private TextView rewardNameText, rewardDescriptionText;
    private String rewardName;
    private String rewardDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_assignment_pop_up);
        rewardNameText = findViewById(R.id.rewardName);
        rewardDescriptionText = findViewById(R.id.rewardDescription);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            rewardName = null;
            rewardDescription = null;
        } else {
            rewardName = extras.getString(EXTRA_REWARD_NAME);
            rewardDescription = extras.getString(EXTRA_REWARD_DESCRIPT);
        }
        try {
            rewardNameText.setText("Reward Name: " + rewardName);
            rewardDescriptionText.setText("Reward Description:\n"+ rewardDescription);

        } catch (Exception e){
            Toast.makeText(RewardAssignmentPopUp.this, "Error! " + e.getMessage(), Toast.LENGTH_LONG ).show();
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*1.2),(int) (height*.7));
    }

    public void onClaim(View view) {
    }
}