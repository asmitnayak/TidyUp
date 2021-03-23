package com.example.android.tidyup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class LeaveGroup extends AppCompatActivity {
    private RecyclerView myRecyclerView;
    private RVAdapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private ArrayList<GroupListObject> myGroupList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_group);
        createGroupList();
        buildRecylcerView();
        myAdapter.setItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LeaveGroup.this);
                builder.setMessage("Are you sure that you want to leave the group?");
                builder.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(position);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });
    }
    public void createGroupList(){
        myGroupList = new ArrayList<>();
        myGroupList.add(new GroupListObject("Apt201",new Button(this)));
        myGroupList.add(new GroupListObject("Apt501",new Button(this))) ;
    }
    public void buildRecylcerView(){
        myRecyclerView = findViewById(R.id.recyclerview);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);
        myAdapter = new RVAdapter(myGroupList);
        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(myAdapter);
    }
    public void removeItem(int position){
        myGroupList.remove(position);
        myAdapter.notifyDataSetChanged();
    }
}


