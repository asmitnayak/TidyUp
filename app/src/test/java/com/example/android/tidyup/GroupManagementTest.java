package com.example.android.tidyup;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runners.model.InitializationError;
//import org.mockito.Mock;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
public class GroupManagementTest {
    private GroupManagement groupManage;
//
//    @Rule
//    public TestRule rule = new InstantTaskExecutorRule();
//
    @Before
    public void setUp(){
       groupManage = new GroupManagement();
       groupManage.setUpEmulator();
        groupManage.execute();
       System.out.print("something");
   }
   @Test
    public void getGroup_invalid_code() {
       String s = GroupManagement.getGroup("N234j2");
       assertTrue(GroupManagement.getGroup("N234j2") == null);
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