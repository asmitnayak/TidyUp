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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;


import java.util.Arrays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class TaskManagment extends AsyncTask<Void, Void, Void> {

    private static final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private static Map<String, Map<String, List<String>>> taskListDB; // list of tasks
    private static Map<String, Map<String, List<String>>> taskDB; // will store the actual info of tasks

    private static Map<String, TaskItem> taskMap;
    private static ArrayList<TaskItem> taskList;
    private static HashMap<String, Object> userMap = new HashMap<>();
    private static Map<String, Map<String, TaskItem>> taskItems;

    private static final String COLLECTIONPATH_TASK = "Task";
    private static final String DOCUMENTPATH_TASKS = "Tasks";
    private static final DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_TASK).document(DOCUMENTPATH_TASKS);


    public static int addTaskItem(String taskName, String person, int pointValue, String priority, String dateToBeCompleted, String repetition) {
        TaskItem addTask = new TaskItem(taskName, person, pointValue, priority, dateToBeCompleted, repetition);

        userMap = UserManagement.getUserDetails();
        Object userGroup = userMap.get("Group");

        DocumentReference docRef = fFirestore.collection("task").document(userGroup.toString());

        docRef.update(taskName, addTask);

        return 1;
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
    public static void removeTaskFromGroup(String groupID, String taskName){
        if(taskListDB.containsKey(groupID)){
            ArrayList<String> taskList = (ArrayList<String>) taskListDB.get(groupID);
            if(taskList.contains(taskName))
                taskList.remove(taskName);

            TaskManagment.Tasks task = new TaskManagment.Tasks(taskListDB);
            fFirestore.collection(COLLECTIONPATH_TASK).document(DOCUMENTPATH_TASKS).set(task);

        }
    }

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
