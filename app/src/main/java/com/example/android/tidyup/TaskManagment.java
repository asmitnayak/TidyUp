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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;


import java.util.Arrays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TaskManagment extends AsyncTask<Void, Void, Void> {

    private static final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private static Map<String, Map<String, List<String>>> taskListDB; // list of tasks
    private static Map<String, Map<String, List<String>>> taskDB; // will store the actual info of tasks

    private static Map<String, TaskItem> taskMap;
    private static ArrayList<TaskItem> taskList;
    private static HashMap<String, Object> userMap = new HashMap<>();
    private static Map<String, Map<String, Object>> taskItems;
    private static Map<String, Map<String, Object>> displayMap;

    private static final String COLLECTIONPATH_TASK = "Task";
    private static final String DOCUMENTPATH_TASKS = "Tasks";
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


    public static int addTaskItem(String taskName, String person, int pointValue, String priority, String dateToBeCompleted, String repetition) {
        TaskItem addTask = new TaskItem(taskName, person, pointValue, priority, dateToBeCompleted, repetition);

        userMap = UserManagement.getUserDetails();
        Object userGroup = userMap.get("Group");
        String groupId = GroupManagement.getGroupID(userGroup.toString());

        DocumentReference docRef = fFirestore.collection("task").document(userGroup.toString());

        docRef.update(taskName, addTask);

        //taskItems.put(userGroup.toString(), addTask);
        if(displayMap == null){
            updateDocRef(groupId);
            if(displayMap == null){
                return -1;
            }
        }

        Map<String, Object> add = displayMap.get(groupId);
        add.put(taskName, addTask);
        displayMap.put(groupId, add);

        return 1;
    }

    public static Map<String, Map<String, Object>> getGroupTaskMap(String groupName) {
        userMap = UserManagement.getUserDetails();
        Object userGroup = userMap.get("Group");
        String groupID = GroupManagement.getGroupID(userGroup.toString());
        Map<String, Map<String, Object>> groupTaskMap;
        if (displayMap == null) {
            updateDocRef(groupID);
            if (displayMap == null) {
                return null;
            }
        }
        if (displayMap.containsKey(userMap.toString())) {
            groupTaskMap = new HashMap<>();
            groupTaskMap = (HashMap) displayMap.get(userMap);
        } else {
            groupTaskMap = new HashMap<>();
        }
        return groupTaskMap;
    }
//        Map<String, Map<String, Object>> returnMap = new Map<String, Map<String, Object>>();
//        final Map<String, Object>[] sampleMap = new Map[]{new HashMap<String, Object>()};
//
//        userMap = UserManagement.getUserDetails();
//        Object userGroup = userMap.get("Group");
//        DocumentReference docRef = fFirestore.collection("task").document(userGroup.toString());
//        final String[] taskName = new String[1];
//        final String[] dateToBeCompleted = new String[1];
//        final String[] priority = new String[1];
//        final String[] repetition = new String[1];
//        final String[] person = new String[1];
//        final String[] reward = new String[1];
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//    @Override
//
//    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//        if (task.isSuccessful()) {
//            DocumentSnapshot document = task.getResult();
//            //System.out.println("DocumentSnapshot data: " + document.getString("taskName"));
//            sampleMap[0] = document.getData();
//            for (Map.Entry<String,Object> entry : sampleMap[0].entrySet()){
//                taskName[0] = TaskPage.getTaskName(entry.getValue().toString());
//                person[0] = TaskPage.getPersonStr(entry.getValue().toString());
//                dateToBeCompleted[0] = TaskPage.getDate(entry.getValue().toString());
//                priority[0] = TaskPage.getPriority(entry.getValue().toString());
//                repetition[0] = TaskPage.getRepetition(entry.getValue().toString());
//                reward[0] = TaskPage.getReward(entry.getValue().toString());
//
//                TaskItem add = new TaskItem(new String(taskName[0]), new String(person[0]), Integer.valueOf(reward[0]), new String(priority[0]),
//                        new String(dateToBeCompleted[0]), new String(repetition[0]));
//                returnMap.put(taskName[0], add);
//            }
//
//        }
//
//    }
//        });

       // return returnMap;
   // }
//        Map<String, Map<String, Object>> groupTaskMap;
//        userMap = UserManagement.getUserDetails();
//        Object userGroup = userMap.get("Group");
//
//
//        DocumentReference docRef = fFirestore.collection("task").document(userGroup.toString());
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    String name = document.getString(KEY_TASKNAME);
//                    String person = document.getString(KEY_PERSON);
//                    String point = document.getString(KEY_POINT);
//                    String priority = document.getString(KEY_PRIORITY);
//                    String repetition = document.getString(KEY_REPETITION);
//                    String date = document.getString(KEY_DATE);
//                    System.out.println("DocumentSnapshot data: " + document.getData());
//                }
//            }
//        });
//
//        return null;
//    }

//        DocumentReference docRef = fFirestore.collection("task").document(userGroup.toString());
//
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                    } else {
//                        //Log.d(TAG, "No such document");
//                    }
//                } else {
//                    //Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });
//        return groupTaskMap;



//        DocumentSnapshot test = docRef.get();
//        Map<String, Object> taskMap = test.getData();

//        Map<String, List<String>> groupTaskMap;
//        if(taskListDB == null){
//            readGroupTaskDB();
//            if(taskListDB == null){
//                return null;
//            }
//        }
//        if(taskListDB.containsKey(groupID)){
//            groupTaskMap = new HashMap<>(taskListDB.get(groupID));
//        }else{
//            groupTaskMap = new HashMap<>();
//        }
//        return groupTaskMap;


    public static HashMap<String, Object> getGroupTaskDetails(){
//        docRef.get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        map = document.getData();
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });
        return (HashMap<String, Object>) userMap;
    }

    protected void setGroupName(String groupName){
        this.groupName = groupName;
    }

    protected static void updateDocRef(String groupID){
        readGroupTaskDB(groupID);
    }
    public static void readGroupTaskDB( String groupID){

        docRef = fFirestore.collection(COLLECTIONPATH_TASK).document(groupID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                TaskManagment.Tasks taskDocument = documentSnapshot.toObject(TaskManagment.Tasks.class);
                displayMap = taskDocument.TaskMap;
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Log.w("TaskFirebase", "Error reading document", e);
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
    public static void removeTaskFromGroup(String groupID, String taskName){
        if(displayMap.containsKey(groupID)){
            ArrayList<String> taskList = (ArrayList<String>) displayMap.get(groupID);
            if(taskList.contains(taskName))
                taskList.remove(taskName);

            TaskManagment.Tasks task = new TaskManagment.Tasks(displayMap);
            fFirestore.collection(COLLECTIONPATH_TASK).document(groupID).set(task);
        }
    }

    private static class Tasks{
        public Map<String, Map<String, Object>> TaskMap = new HashMap<>();
        Tasks(Map<String, Map<String, Object>> customMap){
            TaskMap = new HashMap<>();
            this.TaskMap = customMap;
        }
        public Map<String, Map<String, Object>> getTaskMap() { return this.TaskMap;}
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
