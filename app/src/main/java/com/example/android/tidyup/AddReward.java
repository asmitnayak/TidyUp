package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddReward extends AppCompatActivity {
    EditText rewardName;
    EditText rewardDescription;
    EditText rewardPointVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reward);
        this.rewardName = findViewById(R.id.rewardName);
        this.rewardDescription = findViewById(R.id.rewardDescription);
        this.rewardPointVal = findViewById(R.id.rewardPointVal);
        this.rewardPointVal.getText().toString();
        this.rewardDescription.getText().toString();
        this.rewardName.getText().toString();

    }

    public void onAddReward(View view) {
    }
}