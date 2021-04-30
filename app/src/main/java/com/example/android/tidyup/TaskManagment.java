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
    private static Map<String, Object> displayMap;

    private static final String COLLECTIONPATH_TASK = "task";
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

    public static Map<String, Object> getDisplayMap(){
        return displayMap;
    }

    public static int addTaskItem(String taskName, String person, int pointValue, String dateToBeCompleted, String repetition) {
        TaskItem addTask = new TaskItem(taskName, person, pointValue, dateToBeCompleted, repetition);

        String groupId = (String) UserManagement.getUserDetails().get("GroupID");

        DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_TASK).document(groupId);

        docRef.update(taskName, addTask);

        //taskItems.put(userGroup.toString(), addTask);
        if(displayMap == null){
            updateDocRef(groupId);
            if(displayMap == null){
                return -1;
            }
        }
/*
        Map<String, Object> add = (Map<String, Object> ) displayMap.get(groupId);
        add.put(taskName, addTask);
        displayMap.put(groupId, add);
        docRef.set(displayMap);
 */
        return 1;
    }
    /*
    public static Map<String, Object> getDisplayMap(){
        return displayMap;
    }

     */
    public static Map<String, Object> getGroupTaskMap(String groupName) {
        userMap = UserManagement.getUserDetails();
        String groupID  = (String) userMap.get("GroupID");
        Map<String, Object> groupTaskMap;
        if (displayMap == null) {
            readGroupTaskDB(groupID);
            if (displayMap == null) {
                return null;
            }
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
    public static void readGroupTaskDB(String groupID){

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
    public static void removeTaskFromGroup(String groupID, String taskName){
        DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_TASK).document(groupID);
        Map<String, Object> delete = new HashMap<>();
        delete.put(taskName, FieldValue.delete());
        docRef.update(delete);
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
