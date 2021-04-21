package com.example.android.tidyup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PenaltyAdaptor extends BaseAdapter {
    private Context context;
    private Map<String, List<Object>> penaltyMap;
    private LayoutInflater inflter;
    private List<String> penaltyKey;
    private List penaltyValue;

    PenaltyAdaptor(Context applicationContext, Map<String, List<Object>> penaltyMap){
        this.context = context;
        this.penaltyMap = penaltyMap;
        penaltyKey = new ArrayList<String>(penaltyMap.keySet());
        penaltyValue = new ArrayList(penaltyMap.values());
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return penaltyMap.size();
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
        view = inflter.inflate(R.layout.penalty_list_view_layout, null);

        TextView reward = view.findViewById(R.id.pPenaltyName);
        reward.setText(penaltyKey.get(i));

        return view;
    }
}
