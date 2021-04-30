package com.example.android.tidyup;

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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.SetOptions;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.w3c.dom.Document;
//import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TaskManagementTest {
    private TaskManagment tm;
    FirebaseFirestore mockFirestore;
    FirebaseAuth mockFireAuth;
    FirebaseUser mockFireUser;
    CollectionReference mockCollections;
    DocumentReference mockDocs;
    @Before
    public void setUp() {

        /////////////////////////////////
        //////   MOCKING CLASSES   //////
        /////////////////////////////////

        mockFirestore = mock(FirebaseFirestore.class);
        mockFireAuth = mock(FirebaseAuth.class);
        mockFireUser = mock(FirebaseUser.class);
        mockCollections = mock(CollectionReference.class);
        mockDocs = mock(DocumentReference.class);

        ///////////////////////////////////
        //////   MOCKING FUNCTIONS   //////
        ///////////////////////////////////

        doReturn(mockFireUser).when(mockFireAuth).getCurrentUser();
        doReturn("example").when(mockFireUser).getUid();
        doReturn(mockCollections).when(mockFirestore).collection(anyString());
        doReturn(mockDocs).when(mockCollections).document(anyString());

        //////////////////////////////////
        /////   Test Management   /////
        //////////////////////////////////

        tm = new TaskManagment(mockFireAuth, mockFirestore, "gid1");
        TaskManagment.addTaskItem("task1","id1", 5, "05/05", "none");


    }

    @Test
    public void addAnotherTaskReturnValue(){
        assertNotNull(TaskManagment.addTaskItem("task2","id2", 5, "05/05", "none"));
    }

    @Test
    public void addTaskToGroup() {
        Map<String, Object> data = new HashMap<>();
        TaskItem addItem = new TaskItem("task3","id2", 5, "05/05", "none");
        data.put("task3", addItem);

        TaskItem checkItem = TaskManagment.addTaskItem("task3","id2", 5, "05/05", "none");

        assertEquals(addItem.getTaskName() ,addItem.getTaskName() );
        assertEquals(addItem.getPersonAssignedToTask() ,addItem.getPersonAssignedToTask() );
        assertEquals(addItem.getRewardPenaltyPointValue() ,addItem.getRewardPenaltyPointValue() );
        assertEquals(addItem.getDateToBeCompleted(), checkItem.getDateToBeCompleted());
        assertEquals(addItem.getRepetition() ,addItem.getRepetition());
    }

    @Test
    public void removeTaskFromGroup() {
        TaskManagment.addTaskItem("task3","id2", 5, "05/05", "none");
        Object check = TaskManagment.removeTaskFromGroup("gid1", "task3");
        assertNull(check);
    }

}
