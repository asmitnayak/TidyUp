package com.example.android.tidyup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class Account extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private static final String COLLECTIONPATH_USERS = "Users";
    private static final String TAG = "Account";
    private static final String KEY_USERNAME = "Username";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_USERPOINTS = "UserPoints";
    private static final String KEY_GroupID = "GroupID";
    private static final String KEY_Group = "Group";
    private UserManagement um;
    private RewardsManagement rm;

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private final DocumentReference docRef  = fFirestore.collection(COLLECTIONPATH_USERS).document(fAuth.getCurrentUser().getUid());

    private TextView mName, mEmail, mUserPoints, mGroup;
    private EditText mNewUserEmail;
    private Button mAddMembers, mLeaveGroup;
    private String addedUserID;
    private String grpID;
    private String grpName;

    private ImageView menu;
    private TextView pageTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);

        mName = findViewById(R.id.acName);
        mEmail = findViewById(R.id.acEmail);
        mUserPoints = findViewById(R.id.acUserPoints);

        mGroup = findViewById(R.id.acGroup);
        mNewUserEmail = findViewById(R.id.acNewUserEmail);
        mAddMembers = findViewById(R.id.acAddMembersButton);
        mLeaveGroup = findViewById(R.id.acLeaveGroupButton);

        //action bar
        menu = findViewById(R.id.menu);
        pageTitle = findViewById(R.id.pageTitle);
        pageTitle.setText("Account");

        //test

        // load and display user info on Account Page
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String name = documentSnapshot.getString(KEY_USERNAME);
                    String email = documentSnapshot.getString(KEY_EMAIL);

                    grpID = documentSnapshot.getString(KEY_GroupID);
                    grpName = documentSnapshot.getString(KEY_Group);
                    mName.setText("Username: " + name);
                    mEmail.setText("Email: "+ email);
                    if(!grpID.equals(""))
                       RewardsManagement.resetUserRewards(grpID);
                    String userPoints = documentSnapshot.getString(KEY_USERPOINTS);
                    mUserPoints.setText("UserPoints: " + userPoints);
                    if (!grpName.equals("")){
                        mGroup.setText("Group: " + grpName);
                    } else {
                        mGroup.setText("Group: No Group Yet");
                    }
                }else {
                    Toast.makeText(Account.this, "Document does not Exist", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Account.this, "Error! "+ e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, e.toString());
            }
        });

        docRef.addSnapshotListener(MetadataChanges.EXCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "listen:error", error);
                    return;
                }

                if (documentSnapshot.exists()){
                    String name = documentSnapshot.getString(KEY_USERNAME);
                    String email = documentSnapshot.getString(KEY_EMAIL);

                    grpID = documentSnapshot.getString(KEY_GroupID);
                    grpName = documentSnapshot.getString(KEY_Group);
                    mName.setText("Username: " + name);
                    mEmail.setText("Email: "+ email);
                    if(!grpID.equals(""))
                        RewardsManagement.resetUserRewards(grpID);
                    String userPoints = documentSnapshot.getString(KEY_USERPOINTS);
                    mUserPoints.setText("UserPoints: " + userPoints);
                    if (!grpName.equals("")){
                        mGroup.setText("Group: " + grpName);
                    } else {
                        mGroup.setText("Group: No Group Yet");
                    }
                }else {
                    Toast.makeText(Account.this, "Document does not Exist", Toast.LENGTH_LONG).show();
                }
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Account.this, v);
                popup.setOnMenuItemClickListener( Account.this);
                popup.inflate(R.menu.account_page_menu);
                popup.show();
            }
        });

        mGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str = s.toString();
                String[] arr = str.split(":");
                if(arr.length < 2)
                    return;
                mAddMembers.setEnabled(!arr[1].equalsIgnoreCase("No Group Yet"));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                String[] arr = str.split(":");
                if(arr.length < 2)
                    return;
                mAddMembers.setEnabled(!arr[1].trim().equalsIgnoreCase("No Group Yet"));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /* unneeded for now since using my own action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.account_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.acTaskPage:
                startActivity(new Intent(getApplicationContext(), TaskPage.class));
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
 */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.acTaskPage:
                startActivity(new Intent(getApplicationContext(), TaskPage.class));
                return true;
            case R.id.acRewardsAndPenaltyPage:
//                finish();
                startActivity(new Intent(getApplicationContext(), RewardAndPenalty.class));
                return true;
            default:
                return false;
        }
    }

    public void loguot(View view){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(getApplicationContext(), Login.class));
    }


    public void goToCreateGroupPage(View view){
        Intent intent = new Intent(this, CreateGroup.class);
//        finish();
        startActivity(intent);
    }

    public void goToJoinGroupPage(View view){
        Intent intent = new Intent(this, JoinGroup.class);
        finish();
        startActivity(intent);
    }
    public void gotToGroupSettings(View view){
        Intent intent = new Intent(this, GroupSettings.class);
//        finish();
        startActivity(intent);
    }

    public void goToUpdateUserInfo (View view){
        Intent intent = new Intent(this, UpdateUserInfo.class);
//        finish();
        startActivity(intent);
    }



    public void leaveGroup(View view){
        if (UserManagement.getUserDetails().get("Group").toString().equals("")) {
            AlertDialog.Builder noGroup = new AlertDialog.Builder(Account.this);
            noGroup.setMessage("You are not currently in a group.");
            noGroup.setNeutralButton("Ok", null);
            noGroup.show();
        } else {
            AlertDialog.Builder leaveAlert = new AlertDialog.Builder(Account.this);
            leaveAlert.setMessage("Are you sure you want to leave " + mGroup.getText().toString() + "?");
            leaveAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String userID = fAuth.getCurrentUser().getUid();
                    GroupManagement.removeUserFromGroup(grpID, userID);
                    mGroup.setText(R.string.no_group);
                    docRef.update("Group", "",
                    "GroupID", "");
                }
            });
            leaveAlert.setNegativeButton("Cancel", null);
            leaveAlert.show();
        }
    }

    public void addMembers(View view){
        String newUserEmail = mNewUserEmail.getText().toString().trim();
        if (TextUtils.isEmpty(newUserEmail)){
            mNewUserEmail.setError("New User Email is required");
            return;
        }
        fFirestore.collection(COLLECTIONPATH_USERS)
                .whereEqualTo(KEY_EMAIL, newUserEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            if (task.getResult().getDocuments().size() == 0){
                                Toast.makeText(Account.this, "Error! " + newUserEmail + "Does not have a Tidy Up Account" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                Log.d(TAG, "Error! no user with email: " + newUserEmail);
                                return;
                            }else if (task.getResult().getDocuments().size() > 1){
                                Toast.makeText(Account.this, "Error! more than one user found with email " + newUserEmail, Toast.LENGTH_LONG).show();
                                Log.d(TAG, "Error! more than one user found with email " + newUserEmail);
                                return;
                            }

                            addedUserID = task.getResult().getDocuments().get(0).getId();
                            DocumentReference addedUserDoc = fFirestore.collection(COLLECTIONPATH_USERS).document(addedUserID);
                            GroupManagement.addUserToGroup(grpID, addedUserID, null, GroupManagement.getGroupName(grpID));
                            addedUserDoc.update(KEY_GroupID, grpID,
                                                KEY_Group, grpName);

                        } else {
                            Toast.makeText(Account.this, "Error! " + newUserEmail + "Does not have a Tidy Up Account" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Error! " + task.getException().getMessage() + newUserEmail);
                        }
                    }
                });
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder leaveAlert = new AlertDialog.Builder(Account.this);
        leaveAlert.setMessage("Are you sure you want to quit ?");
        leaveAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAndRemoveTask();
                System.exit(0);
            }
        });
        leaveAlert.setNegativeButton("Cancel", null);
        leaveAlert.show();
    }
}
