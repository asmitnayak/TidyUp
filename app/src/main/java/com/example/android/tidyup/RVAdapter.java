package com.example.android.tidyup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.myViewHolder> {
    private ArrayList<GroupListObject> myGroupList;
    private OnItemClickListener mylistener;
    public interface OnItemClickListener{
        void onDeleteClick(int position);
    }
    public void setItemClickListener(OnItemClickListener listener){
        mylistener = listener;
    }
    public static class myViewHolder extends RecyclerView.ViewHolder{
        public TextView groupName;
        public Button leaveButton;
        public myViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            leaveButton = itemView.findViewById(R.id.leaveButton);

            leaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
    public RVAdapter(ArrayList<GroupListObject> groupList){
        this.myGroupList = groupList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.actvity_group_list_layout, parent, false);
        myViewHolder vh = new myViewHolder(v,mylistener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        GroupListObject currentItem = myGroupList.get(position);
        holder.groupName.setText(currentItem.getGroupName());
        holder.leaveButton = currentItem.getLeaveButton();
    }

    @Override
    public int getItemCount() {
        return myGroupList.size();
    }
}
