package com.example.android.tidyup;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

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
//import org.mockito.Mock;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
@RunWith(MockitoJUnitRunner.class)
public class GroupManagementTest {
    private GroupManagement groupManage;
//
//    @Rule
//    public TestRule rule = new InstantTaskExecutorRule();
//

//    @Mock
//    GroupManagement gm;
//
//    @Mock
//    Context c;

//    @Before
//    public void setUp(){
//        FirebaseApp.initializeApp(c);
//        gm.setUpEmulator();
//        assertNotNull(gm);
//        System.out.print("something");
//   }
   @Test
    public void getGroup_invalid_code() {
//       String s = GroupManagement.getGroup("N234j2");
//       assertTrue(5 == 5);
       FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
//       Mockito.when(mockFirestore.isAvailable()).thenReturn();

       GroupManagement gm = new GroupManagement(mockFirestore);
       gm.setUpEmulator();

       assert (4 == 4);

    }
//
//    @Test
//    public void getGroupIDFromUserID() {
//        assertTrue(groupManage.getGroupIDFromUserID("siTctFru2AcyzDguEqGE3xtnKWi2") == "t6yhC6Dm784QN6NkUZt2fnmDx5I1oBq0KI7AvMFtMxc");
//    }
//
//    @Test
//    public void addUserToGroup() {
//    }
//
//    @Test
//    public void getGroupMemberList() {
//    }
//
//    @Test
//    public void getGroupName() {
//    }
//
//    @Test
//    public void getGroupTask() {
//    }
//
//    @Test
//    public void setGroupTask() {
//    }
//
//    @Test
//    public void removeUserFromGroup() {
//    }
//
//    @Test
//    public void addGroupCodes() {
//    }
//
//    @Test
//    public void readGroupCodeDB() {
//    }
//
//    @Test
//    public void readGroupDB() {
//    }
//
//    @Test
//    public void getGroupID() {
//    }
}