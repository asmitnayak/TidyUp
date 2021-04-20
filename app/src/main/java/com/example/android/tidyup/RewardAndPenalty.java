package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardAndPenalty extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
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

    private ImageView menu;
    private TextView pageTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_and_penatly);
        listView = findViewById(R.id.rewardsList);
        rewardsMap = new HashMap<>();
        rewardsMap = RewardsManagement.getGroupRewardsMap((String) UserManagement.getUserDetails().get("GroupID"));
        /*
        List reward1 = new ArrayList();
        reward1.add("This is a random Description for reward 1");
        reward1.add("5");
        reward1.add("rrkHVlRUIsUpNWaVTgUCbaul0Zq1");
        List reward2 = new ArrayList();
        reward2.add("Reward2 description");
        reward2.add("7");
        reward2.add("rrkHVlRUIsUpNWaVTgUCbaul0Zq1");
        List reward3 = new ArrayList();
        reward3.add("Reward3 description");
        reward3.add("9");
        reward3.add("siTctFru2AcyzDguEqGE3xtnKWi2");
        rewardsMap.put("Reward 1", reward1);
        rewardsMap.put("Reward 2", reward2);
        rewardsMap.put("Reward 2", reward3);
        */

        rewardsAdaptor = new RewardsAdaptor(this, rewardsMap);
        listView.setAdapter(rewardsAdaptor);
        rewardsKey = new ArrayList<String>(rewardsMap.keySet());
        rewardsValue = new ArrayList<List<Object>>(rewardsMap.values());

        menu = findViewById(R.id.menu);
        pageTitle = findViewById(R.id.pageTitle);
        pageTitle.setText("Rewards");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RewardPopUp.class);
                intent.putExtra(EXTRA_REWARD_NAME, (String) rewardsKey.get(position));
                String assignedUserUID = (String) ((List<Object>) rewardsValue.get(position)).get(2);
                String assignedUser = "NA";
                if (assignedUserUID != null){
                     assignedUser = UserManagement.getUserNameFromUID(assignedUserUID);
                }

                intent.putExtra(EXTRA_ASSIGNED_USER, assignedUser);
                intent.putExtra(EXTRA_REWARD_VAL, (String) "" + ((List<Object>) rewardsValue.get(position)).get(1));
                intent.putExtra(EXTRA_REWARD_DESCRIPT, (String) ((List<Object>) rewardsValue.get(position)).get(0));
                startActivity(intent);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(RewardAndPenalty.this, v);
                popup.setOnMenuItemClickListener( RewardAndPenalty.this);
                popup.inflate(R.menu.reward_and_penalty_menu);
                popup.show();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.rpAccount:
                finish();
                startActivity(new Intent(getApplicationContext(), Account.class));
                return true;
            default:
                return false;
        }
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