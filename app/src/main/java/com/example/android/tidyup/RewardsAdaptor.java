package com.example.android.tidyup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardsAdaptor extends BaseAdapter {
    private Context context;
    private Map<String, List<Object>> rewardsMap;
    private LayoutInflater inflter;
    private List<String> rewardsKey;
    private List rewardsValue;

    RewardsAdaptor(Context applicationContext, Map<String, List<Object>> rewardsMap){
        this.context = context;
        this.rewardsMap = rewardsMap;
        rewardsKey = new ArrayList<String>(rewardsMap.keySet());
        rewardsValue = new ArrayList(rewardsMap.values());
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return rewardsMap.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.rewards_list_view_layout, null);

        TextView reward = view.findViewById(R.id.rRewardName);
        reward.setText(rewardsKey.get(i));


        return view;
    }
}
