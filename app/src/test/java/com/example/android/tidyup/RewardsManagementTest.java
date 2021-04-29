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
public class RewardsManagementTest {
    private RewardsManagement rm;

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

//        doReturn(mockFireUser).when(mockFireAuth).getCurrentUser();
//        doReturn("example").when(mockFireUser).getUid();
        doReturn(mockCollections).when(mockFirestore).collection(anyString());
        doReturn(mockDocs).when(mockCollections).document(anyString());

        //////////////////////////////////
        /////   Penalty Management   /////
        //////////////////////////////////

        rm = new RewardsManagement(mockFireAuth, mockFirestore);
        RewardsManagement.addReward("gid1", "This is a reward", "Reward1", 15);
    }

    @Test
    public void getGroupRewardMap(){
        String expectedName  = "Reward1";
        ArrayList<Object> expectedValues = new ArrayList<Object>();
        expectedValues.add("This is a reward");
        expectedValues.add("15");
        expectedValues.add(null);
        HashMap<String, List<Object>> map = (HashMap<String, List<Object>>) RewardsManagement.getGroupRewardsMap("gid1");
        String actualName = (String) RewardsManagement.getGroupRewardsMap("gid1").keySet().toArray()[0];
        ArrayList<Object> actualValues = new ArrayList<>(map.get("Reward1"));
        assertEquals(expectedName, actualName);
        assertEquals(expectedValues, actualValues);

    }

    @Test
    public void getGroupRewardMapNonExistent(){
        assertTrue(RewardsManagement.getGroupRewardsMap("gid2").isEmpty());
    }

    @Test
    public void addAnotherRewardGroup(){
        RewardsManagement.addReward("gid2", "This is a reward", "Reward2", 12);
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Reward2");
        assertEquals(expected, RewardsManagement.getRewardNameList(RewardsManagement.getGroupRewardsMap("gid2")));
    }

    @Test
    public void addRewardReturn(){
        assertEquals(1, RewardsManagement.addReward("gid2", "This is a reward", "Reward2", 12));
    }

    @Test
    public void getNewlyAddedRewardMap(){
        RewardsManagement.addReward("gid2", "This is a reward", "Reward2", 12);
        String expectedName  = "Reward2";
        ArrayList<Object> expectedValues = new ArrayList<Object>();
        expectedValues.add("This is a reward");
        expectedValues.add("12");
        expectedValues.add(null);
        HashMap<String, List<Object>> map = (HashMap<String, List<Object>>) RewardsManagement.getGroupRewardsMap("gid2");
        String actualName = (String) RewardsManagement.getGroupRewardsMap("gid2").keySet().toArray()[0];
        ArrayList<Object> actualValues = new ArrayList<>(map.get("Reward2"));
        assertEquals(expectedName, actualName);
        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void addRewardToExistingRewardList(){
        RewardsManagement.addReward("gid1", "This is a new reward", "Reward3", 3);
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Reward1");
        expected.add("Reward3");
        assertEquals(expected, RewardsManagement.getRewardNameList(RewardsManagement.getGroupRewardsMap("gid1")));
    }
}
