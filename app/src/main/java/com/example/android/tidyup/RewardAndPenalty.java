package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardAndPenalty extends AppCompatActivity {
    private static final String TAG = "RewardAndPenalty";
    private static final String EXTRA_REWARD_NAME = "EXTRA_REWARD_NAME";
    private static final String EXTRA_ASSIGNED_USER = "EXTRA_ASSIGNED_USER";
    private static final String EXTRA_REWARD_VAL = "EXTRA_REWARD_VAL";
    private static final String EXTRA_REWARD_DESCRIPT = "EXTRA_REWARD_DESCRIPT";

    private ListView listView;
    private Map<String, List<Object>> rewardsMap;
    private RewardsAdaptor rewardsAdaptor;
    private List rewardsKey;
    private List rewardsValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_and_penatly);
        listView = findViewById(R.id.rewardsList);
        rewardsMap = new HashMap<>();
        List reward1 = new ArrayList();
        reward1.add("This is a random Description for reward 1");
        reward1.add(5);
        reward1.add("rrkHVlRUIsUpNWaVTgUCbaul0Zq1");
        List reward2 = new ArrayList();
        reward2.add("Reward2 description");
        reward2.add(7);
        reward2.add("rrkHVlRUIsUpNWaVTgUCbaul0Zq1");
        List reward3 = new ArrayList();
        reward3.add("Reward3 description");
        reward3.add(9);
        reward3.add("siTctFru2AcyzDguEqGE3xtnKWi2");
        rewardsMap.put("Reward 1", reward1);
        rewardsMap.put("Reward 2", reward2);
        rewardsMap.put("Reward 3", reward3);

        rewardsAdaptor = new RewardsAdaptor(this, rewardsMap);
        listView.setAdapter(rewardsAdaptor);
        rewardsKey = new ArrayList<String>(rewardsMap.keySet());
        rewardsValue = new ArrayList<List<Object>>(rewardsMap.values());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplication(), RewardPopUp.class);
                intent.putExtra(EXTRA_REWARD_NAME, (String) rewardsKey.get(position));
                String assignedUserUID = (String) ((List<Object>) rewardsValue.get(position)).get(2);

                String assignedUser = UserManagement.getUserNameFromUID(assignedUserUID);
                //Todo Debug why it returns null first time
                if (assignedUser == null){
                    Log.d(TAG, "Error User does not Exist");
                }
                Toast.makeText(RewardAndPenalty.this, assignedUser, Toast.LENGTH_LONG).show();
                intent.putExtra(EXTRA_ASSIGNED_USER, assignedUser);
                intent.putExtra(EXTRA_REWARD_VAL, (String) "" + ((List<Object>) rewardsValue.get(position)).get(1));
                intent.putExtra(EXTRA_REWARD_DESCRIPT, (String) ((List<Object>) rewardsValue.get(position)).get(0));
                startActivity(intent);
            }
        });
    }

    public void OnAddReward(View view) {
        Intent intent = new Intent(this, AddReward.class);
        startActivity(intent);
    }

    public void OnAddPenalty(View view) {

    }

    public void OnAssignPenalty(View view) {

    }
}