package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

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
    private static final String EXTRA_PENALTY_NAME = "EXTRA_PENALTY_NAME";
    private static final String EXTRA_PENALTY_DESCRIPT = "EXTRA_PENALTY__DESCRIPT";

    private ListView rewardsListView;
    private Map<String, List<Object>> rewardsMap;
    private RewardsAdaptor rewardsAdaptor;
    private List rewardsKey;
    private List rewardsValue;

    private ListView penaltyListView;
    private Map<String, List<Object>> penaltyMap;
    private PenaltyAdaptor penaltyAdaptor;
    private List penaltyKey;
    private List penaltyValue;

    private ImageView menu;
    private TextView pageTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_and_penatly);
        String groupID = (String) UserManagement.getUserDetails().get("GroupID");

        rewardsListView = findViewById(R.id.rewardsList);
        rewardsMap = new HashMap<>();
        rewardsMap = RewardsManagement.getGroupRewardsMap(groupID);
        rewardsAdaptor = new RewardsAdaptor(this, rewardsMap);
        rewardsListView.setAdapter(rewardsAdaptor);
        rewardsKey = new ArrayList<String>(rewardsMap.keySet());
        rewardsValue = new ArrayList<List<Object>>(rewardsMap.values());

        penaltyListView = findViewById(R.id.penaltyList);
        penaltyMap = new HashMap<>();
        penaltyMap = PenaltyManagement.getGroupPenaltyMap(groupID);
        if (penaltyMap != null) {
            penaltyAdaptor = new PenaltyAdaptor(this, penaltyMap);
            penaltyListView.setAdapter(penaltyAdaptor);
            penaltyKey = new ArrayList<String>(penaltyMap.keySet());
            penaltyValue = new ArrayList<List<Object>>(penaltyMap.values());
        }

        menu = findViewById(R.id.menu);
        pageTitle = findViewById(R.id.pageTitle);
        pageTitle.setText("Rewards and Penalties");

        rewardsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        penaltyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PenaltyPopUp.class);
                intent.putExtra(EXTRA_PENALTY_NAME, (String) penaltyKey.get(position));
                String assignedUserUID = (String) ((List<Object>) penaltyValue.get(position)).get(1);
                String assignedUser = "NA";
                if (assignedUserUID != null){
                    assignedUser = UserManagement.getUserNameFromUID(assignedUserUID);
                }

                intent.putExtra(EXTRA_ASSIGNED_USER, assignedUser);
                intent.putExtra(EXTRA_PENALTY_DESCRIPT, (String) ((List<Object>) penaltyValue.get(position)).get(0));
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