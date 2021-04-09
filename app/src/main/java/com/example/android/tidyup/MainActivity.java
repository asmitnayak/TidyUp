package com.example.android.tidyup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firestore.v1.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DocSnippets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GroupManagement gm = new GroupManagement();
        UserManagement um = new UserManagement();
        RewardsManagement rm = new RewardsManagement();
        rm.execute();
        gm.execute();
        um.execute();
        testAddReward();
        //testResetAllUserPoints();
//        fireStoreAdd2Doc();
        Button btn = (Button) findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testAddReward();
            }
        });
//        GroupManagement.readGroupCodeDB();
//        fireStoreAddObject();


    }


    private void fireStoreAdd2Doc(){
        C_D ab = new C_D("Hello2", "World2");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("first-time").document("id-specific").set(ab, SetOptions.merge());
    }

    private void fireStoreAddObject(){
        A_B ab = new A_B("Hello", Arrays.asList("World", "World2", "World3"));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("first-time").document("id-specific3").set(ab, SetOptions.merge());
    }

    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("testing/tested");
    private void fireStoreDBDocHandling(){
        Map<String, Object> entry = new HashMap<>();
        entry.put("Place", "New York");
        mDocRef.set(entry).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Log.d(TAG, "Document added");
                else
                    Log.d(TAG, "Document addition failed");
            }
        });
    }

    private void fireStoreDBHandling(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        // Create a new user with a first, middle, and last name
        user = new HashMap<>();
        user.put("first", "Alan");
        user.put("middle", "Mathison");
        user.put("last", "Turing");
        user.put("born", 1912);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    //This test should create a group and check if the group is in the database with correct info
    private void testCreateGroupDB(){

    }
    //This test should make sure new member is added to the group given group id
    private void testJoinGroupViaCode(String userID, String groupID){

    }
    //This test should retrieve group info from the database based on group ID
    private void testGetGroupID(){

    }
    //This test should add a user to a group and check to see if it was added to the list in the db
    private void testAddUserToGroup(){

    }
    //This test should make sure all members are present in the list in the db
    private void testAddMultipleUsersToGroup(){

    }
    //This test should make sure the given user is no longer in the db list
    private void testRemoveUserFromGroup(){

    }
    //This test should make sure all removed members are no longer in the db list
    private void testRemoveMultipleUsersFromGroup(){

    }
    //This test should make sure the users points increase in the db when a task is completed
    private void testUserPointIncreaseOnTaskComplete(){

    }
    //This test should make sure the task completed is no longer in the group db
    private void testOnTaskCompleteTaskRemovedFromGroupDB(){

    }
    //This test should make sure the task completed is no longer in the user db
    private void testOnTaskCompleteTaskRemovedFromUserDB(){

    }
    //This test should make sure all new tasks are added to the list in the group db
    private void testOnCreateTaskAddedToGroupDB(){

    }
    //This test should make sure all new tasks assigned to a specific user are added to the list
    // in the user db
    private void testOnCreateTaskAddedToUserDB(){

    }
    //This test should get the full task list from db
    private void testGetTaskListFromDB(){

    }
    //This test should get the full task list from db
    private void testAddReward(){

        RewardsManagement rm = new RewardsManagement();
        rm.execute();
        RewardsManagement.addReward("test", "test reward description" , "Test Reward Name", 15);

    }

    private void testResetAllUserPoints(){
        GroupManagement gm = new GroupManagement();
        gm.execute();
        UserManagement um = new UserManagement();
        um.execute();
        UserManagement.resetAllUserPoints("t6yhC6Dm784QN6NkUZt2fnmDx5I1oBq0KI7AvMFtMxc=");
    }

    private void testAssignReward(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Task<DocumentSnapshot> doc = db.collection("Rewards_penalties").document("Rewards").get();
        UserManagement.resetAllUserPoints("t6yhC6Dm784QN6NkUZt2fnmDx5I1oBq0KI7AvMFtMxc=");
    }




    class A_B{

        String a;
        List<String> b;
        Map<String, Object> nestedData = new HashMap<>();

        A_B(){};
        A_B(String a, List<String> b){
            this.a = a;
            this.b = b;
            this.nestedData.put("g1", Arrays.asList("a","b","c"));
            this.nestedData.put("g2", Arrays.asList("a2","b2","c2"));
        }

        public String getA(){
            return this.a;
        }
        public List<String> getB(){
            return this.b;
        }
        public Map<String, Object> getNestedData(){
            return this.nestedData;
        }
    }
    class C_D{

        String a, b;

        C_D(){};
        C_D(String a, String b){
            this.a = a;
            this.b = b;
        }

        public String getC(){
            return this.a;
        }
        public String getD(){
            return this.b;
        }
    }

}