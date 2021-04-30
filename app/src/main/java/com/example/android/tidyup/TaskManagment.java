package com.example.android.tidyup;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;
import com.google.gson.Gson;


import java.lang.reflect.Type;
import java.util.Arrays;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TaskManagment extends AsyncTask<Void, Void, Void> {

    private static FirebaseAuth fAuth;
    private static FirebaseFirestore fFirestore;
    private static Map<String, Map<String, List<String>>> taskListDB; // list of tasks
    private static Map<String, Map<String, List<String>>> taskDB; // will store the actual info of tasks

    private static Map<String, TaskItem> taskMap;
    private static ArrayList<TaskItem> taskList;
    private static HashMap<String, Object> userMap = new HashMap<>();
    private static Map<String, Map<String, Object>> taskItems;
    private static Map<String, Object> displayMap;
    private static String groupID;
    private static final String COLLECTIONPATH_TASK = "task";
    private static  DocumentReference docRef;

    private static final String TAG = "task";
    private static final String KEY_TASKNAME= "taskName";
    private static final String KEY_PERSON = "personAssignedToTask";
    private static final String KEY_POINT= "rewardPenaltyPointValue";
    private static final String KEY_PRIORITY= "priority";
    private static final String KEY_REPETITION= "repetition";
    private static final String KEY_DATE= "dateToBeCompleted";
    private static final String KEY_CHECKED= "isChecked"; //might need to be boolean

    private static String groupName;

    public TaskManagment(){
        fFirestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        displayMap = new HashMap<>();

        groupID = (String) UserManagement.getUserDetails().get("GroupID");
        docRef = fFirestore.collection(COLLECTIONPATH_TASK).document(groupID);
    }

    // Testing only
    public TaskManagment(FirebaseAuth mockFireAuth, FirebaseFirestore mockFirestore, String gid1) {
        fFirestore = mockFirestore;
        fAuth = mockFireAuth;
        docRef = fFirestore.collection(COLLECTIONPATH_TASK).document(gid1);
        displayMap = new HashMap<>();
        groupID = gid1;
        docRef = fFirestore.collection(COLLECTIONPATH_TASK).document(groupID);
    }



    public static Map<String, Object> getDisplayMap(){
        return displayMap;
    }

    public static TaskItem addTaskItem(String taskName, String person, int pointValue, String dateToBeCompleted, String repetition) {
        TaskItem addTask = new TaskItem(taskName, person, pointValue, dateToBeCompleted, repetition);

        String groupId = (String) UserManagement.getUserDetails().get("GroupID");
//        fFirestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_TASK).document(groupId);

        if (docRef != null)
            docRef.update(taskName, addTask);
/*
        //taskItems.put(userGroup.toString(), addTask);
        if(displayMap == null){
            readGroupTaskDB(groupID);
        }
/*
        Map<String, Object> add = (Map<String, Object> ) displayMap.get(groupId);
        add.put(taskName, addTask);
        displayMap.put(groupId, add);
        docRef.set(displayMap);
 */
        return addTask;
    }
    /*
    public static Map<String, Object> getDisplayMap(){
        return displayMap;
    }

     */
    public static Map<String, Object> getGroupTaskMap(String groupName) {
        userMap = UserManagement.getUserDetails();
        //String groupID  = (String) userMap.get("GroupID");
        Map<String, Object> groupTaskMap;
        if (displayMap == null) {
            readGroupTaskDB(groupID);
        }
        /*
        if (displayMap.containsKey(userMap.toString())) {
            groupTaskMap = new HashMap<>();
            groupTaskMap = (HashMap) displayMap.get(userMap);
        } else {
            groupTaskMap = new HashMap<>();
        }

         */
        return displayMap;
    }


    public static HashMap<String, Object> getGroupTaskDetails(){
        return (HashMap<String, Object>) userMap;
    }

    protected void setGroupName(String groupName){
        this.groupName = groupName;
    }

    public static void readGroupTaskDB(String groupID){
        fFirestore = FirebaseFirestore.getInstance();
        CollectionReference colRef = fFirestore.collection(COLLECTIONPATH_TASK);

        docRef = fFirestore.collection(COLLECTIONPATH_TASK).document(groupID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot==null) {
                    displayMap = new HashMap<>();
                    return;
                }
                TaskManagment.Tasks taskDocument = documentSnapshot.toObject(TaskManagment.Tasks.class);
                displayMap = taskDocument.taskMap;
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Log.w("TaskFirebase", "Error reading document", e);
            }
        });

        docRef.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "listen:error", error);
                    return;
                }
                if(documentSnapshot==null || !documentSnapshot.exists()) {
                    displayMap = new HashMap<>();
                    return;
                }
                TaskManagment.Tasks taskDocument = documentSnapshot.toObject(TaskManagment.Tasks.class);
                displayMap = taskDocument.taskMap;
            }
        });

    }

    private interface FirestoreCallback{
        void onCallback(TaskManagment.Tasks taskDocument);
    }
    /*public static ArrayList<String> getGroupTaskList(String groupID){
        if(taskListDB == null){
            readGroupTaskDB();
            if(taskListDB == null)
                return null;
        }
        if(!taskListDB.containsKey(groupID)) {
            return null;
        }
        else {
            ArrayList<String> returnList = new ArrayList<>(taskListDB.get(groupID));
            returnList.remove(0);
            returnList.remove(1);
            return returnList;
        }
    }*/
    public static Object removeTaskFromGroup(String groupID, String taskName){
//        fFirestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_TASK).document(groupID);
        Map<String, Object> delete = new HashMap<>();
        Object returnObj = delete.put(taskName, FieldValue.delete());
        docRef.update(delete);
        return returnObj;
    }

    private static class Tasks{
        public Map<String, Object> taskMap = new HashMap<>();

        Tasks(){}

        Tasks(Map<String, Object> customMap){
            taskMap = new HashMap<>();
            this.taskMap = customMap;
        }
        public Map<String, Object> getTaskMap() { return this.taskMap;}
    }

    @Override
    protected void onPreExecute() {
        readGroupTaskDB((String) UserManagement.getUserDetails().get("GroupID"));
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        return null;
    }
}
