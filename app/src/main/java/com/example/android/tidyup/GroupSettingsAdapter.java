package com.example.android.tidyup;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupSettingsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    String groupID;
    ArrayList<String> members;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private DocumentReference docRef;

    public GroupSettingsAdapter(Context applicationContext, ArrayList<String> members, String groupID) {
        this.context = context;
        this.members = members;
        this.groupID = groupID;
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
        docRef = fFirestore.collection("Users").document(members.get(i));
        if(members.get(i) != null){
            docRef = fFirestore.collection("Users").document(members.get(i));
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        String name = documentSnapshot.getString("Name");
                        memberName.setText(name);
                    }else {
                        return;
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("GroupSettings", "Error reading document", e);
                }
            });
        }
        Button delete = (Button) view.findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                GroupManagement.removeUserFromGroup(groupID, members.get(i));
                members.remove(i);
            }
        });
        return view;
    }
}
