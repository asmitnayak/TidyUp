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
public class PenaltyManagementTest {
    private PenaltyManagement pm;

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
        /////   Penalty Management   /////
        //////////////////////////////////

        pm = new PenaltyManagement(mockFirestore, mockFireAuth, "gid1", "group1", "example");
        PenaltyManagement.addPenalty("gid1", "This is a penalty", "Penalty1");
    }

    @Test
    public void getGroupPenaltyMap(){
        String expectedName  = "Penalty1";
        ArrayList<Object> expectedValues = new ArrayList<Object>();
        expectedValues.add("This is a penalty");
        expectedValues.add(null);
        HashMap<String, List<Object>> map = (HashMap<String, List<Object>>) PenaltyManagement.getGroupPenaltyMap("gid1");
        String actualName = (String) PenaltyManagement.getGroupPenaltyMap("gid1").keySet().toArray()[0];
        ArrayList<Object> actualValues = (ArrayList<Object>) map.get("Penalty1");
        assertEquals(expectedName, actualName);
        assertEquals(expectedValues, actualValues);

    }
    @Test
    public void addAnotherPenaltyGroup(){
        PenaltyManagement.addPenalty("gid2", "This is a penalty", "Penalty2" );
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Penalty2");

        assertEquals(expected, PenaltyManagement.getPenaltyNameList(PenaltyManagement.getGroupPenaltyMap("gid2")));
    }

}

