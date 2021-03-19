package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class LeaveGroup extends AppCompatActivity {
    private RecyclerView myRecyclerView;
    private RVAdapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private ArrayList<GroupItems> myGroupList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_group);
        myGroupList = new ArrayList<>();
        myGroupList.add(new GroupItems("Apt201",new Button(this)));
        myGroupList.add(new GroupItems("Apt501",new Button(this))) ;

        myRecyclerView = findViewById(R.id.recyclerview);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);
        myAdapter = new RVAdapter(myGroupList);
        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(myAdapter);
        myAdapter.setItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }
    public void removeItem(int position){
        myGroupList.remove(position);
        myAdapter.notifyDataSetChanged();
    }
}


