package com.example.android.tidyup;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupSettingsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    ArrayList<String> members;

    public GroupSettingsAdapter(Context applicationContext, ArrayList<String> members) {
        this.context = context;
        this.members = members;
        inflter = (LayoutInflater.from(applicationContext));

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.member_list_view_layout, null);
        TextView memberName = (TextView) view.findViewById(R.id.memberName);
        memberName.setText(members.get(i)); //probably get users name from group
        Button delete = (Button) view.findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                //GroupManagement.removeUserToGroup("fjfhghgd", members.get(i));
                members.remove(i);
            }
        });
        return view;
    }
}
