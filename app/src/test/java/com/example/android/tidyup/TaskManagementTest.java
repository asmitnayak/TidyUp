package com.example.android.tidyup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TaskManagementTest {
    private TaskManagment tm;
    @Before
    public void setUp() {

        /////////////////////////////////
        //////   MOCKING CLASSES   //////
        /////////////////////////////////

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        FirebaseAuth mockFireAuth = mock(FirebaseAuth.class);
        FirebaseUser mockFireUser = mock(FirebaseUser.class);
        CollectionReference mockCollections = mock(CollectionReference.class);
        DocumentReference mockDocs = mock(DocumentReference.class);

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
        assertEquals(1, TaskManagment.addTaskItem("task2","id2", 5, "05/05", "none"));
    }

    @Test
    public void addTaskToGroup() {
        TaskManagment.addTaskItem("task3","id2", 5, "05/05", "none");
        String expectedName = "task3";
        ArrayList<Object> expectedTaskValues = new ArrayList<Object>();
        expectedTaskValues.add("task3");
        expectedTaskValues.add("id2");
        expectedTaskValues.add(5);
        expectedTaskValues.add("05/05");
        expectedTaskValues.add("none");

    }

    @Test
    public void removeTaskFromGroup() {

    }

}
