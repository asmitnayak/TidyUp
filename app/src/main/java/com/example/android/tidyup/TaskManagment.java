package com.example.android.tidyup;

import android.os.AsyncTask;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import androidx.annotation.NonNull;

public class TaskManagment extends AsyncTask<Void, Void, Void> {
    private static final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private static Map<String, Map<String, List<String>>> taskListDB; // list of tasks
    private static Map<String, Map<String, List<String>>> taskDB; // will store the actual info of tasks
    private static final String COLLECTIONPATH_TASK = "Task";
    private static final String DOCUMENTPATH_TASKS = "Tasks";
    private static final DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_TASK).document(DOCUMENTPATH_TASKS);

    public static int addTaskItem(String groupID, String taskName, String person, int pointValue, String priority, String dateToBeCompleted, String repetition) {
        if(taskListDB.containsKey(groupID)) {
            Map<String, List<String>> groupTaskMap = new HashMap<>(taskListDB.get(groupID));
            if(groupTaskMap.containsKey(taskName)){
                return 0;
            } else {
                groupTaskMap.put(taskName, Arrays.asList(new String[]{person, String.valueOf(pointValue), priority, dateToBeCompleted, repetition, null}));
                taskListDB.put(groupID, groupTaskMap);
                // Tasks task = new Tasks(taskDB);
                // docRef.collection(COLLECTIONPATH_TASK).document(DOCUMENTPATH_TASKS).set(task);
                // return 1;
            }
        } else {
            Map<String, List<String>> groupTaskMap = new HashMap<>();
            groupTaskMap.put(taskName, Arrays.asList(new String[]{person, String.valueOf(pointValue), priority, dateToBeCompleted, repetition, null}));
            taskListDB.put(groupID, groupTaskMap);
            // Tasks task = new Tasks(taskDB);
            // docRef.collection(COLLECTIONPATH_TASK).document(DOCUMENTPATH_TASKS).set(task);
            // return 1;
        }
        TaskManagment.Tasks tasks = new TaskManagment.Tasks(taskListDB);
        fFirestore.collection(COLLECTIONPATH_TASK).document(DOCUMENTPATH_TASKS).set(tasks);
        return 1;

        // TaskItem addTaskItem = new TaskItem(taskName, person, pointValue, priority, dateToBeCompleted, repetition);
        // taskItems.add(addTaskItem);
    }

    public static Map<String, List<String>> getGroupTaskMap(String groupID){
        Map<String, List<String>> groupTaskMap;
        if(taskListDB == null){
            readGroupTaskDB();
            if(taskListDB == null){
                return null;
            }
        }
        if(taskListDB.containsKey(groupID)){
            groupTaskMap = new HashMap<>(taskListDB.get(groupID));
        }else{
            groupTaskMap = new HashMap<>();
        }
        return groupTaskMap;
    }

    public static void readGroupTaskDB(){
        DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_TASK).document(DOCUMENTPATH_TASKS);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                TaskManagment.Tasks taskDocument = documentSnapshot.toObject(TaskManagment.Tasks.class);
                taskListDB = taskDocument.TaskMap;
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Log.w("TaskFirebase", "Error reading document", e);
            }
        });

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

    private static class Tasks{
        public Map<String, Map<String, List<String>>> TaskMap = new HashMap<>();
        Tasks(Map<String, Map<String, List<String>>> customMap){
            TaskMap = new HashMap<>();
            this.TaskMap = customMap;
        }
        public Map<String, Map<String, List<String>>> getTaskMap() { return this.TaskMap;}
    }

    @Override
    protected void onPreExecute() {
        readGroupTaskDB();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }
}
