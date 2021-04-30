package com.example.android.tidyup;

import android.util.Base64;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GroupManagementTest {

    private GroupManagement gm;
    private RewardsManagement rm;
    private PenaltyManagement pm;
    private FirebaseFirestore mockFirestore;
    private FirebaseAuth mockFireAuth;
    private FirebaseUser mockFireUser;
    private CollectionReference mockCollections;
    private DocumentReference mockDocs;

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
        Task mockTask = mock(Task.class);

        ///////////////////////////////////
        //////   MOCKING FUNCTIONS   //////
        ///////////////////////////////////

        doReturn(mockFireUser).when(mockFireAuth).getCurrentUser();
        doReturn("example").when(mockFireUser).getUid();
        doReturn(mockCollections).when(mockFirestore).collection(anyString());
        doReturn(mockDocs).when(mockCollections).document(anyString());
        doReturn(mockTask).when(mockDocs).delete();
        doReturn(mockTask).when(mockTask).addOnSuccessListener(any(OnSuccessListener.class));

        //////////////////////////////////
        //////   Group Management   //////
        //////////////////////////////////

        gm = new GroupManagement(mockFirestore, mockFireAuth, "gid1", "group1", "example");
        GroupManagement.addGroupCodes("gid1","gc1");
        GroupManagement.addGroupCodes("gid2","gc2");
        GroupManagement.addGroupCodes("gid3","gc3");
        GroupManagement.addGroupCodes("gid1","gc4");

        GroupManagement.addUserToGroup("gid1","example11","gc22","group1");
        GroupManagement.addUserToGroup("gid2","test","gc21","group2");

        ///////////////////////////////////
        //////   Reward Management   //////
        ///////////////////////////////////
        rm = new RewardsManagement(mockFireAuth, mockFirestore);

        ////////////////////////////////////
        //////   Penalty Management   //////
        ////////////////////////////////////
        pm = new PenaltyManagement(mockFirestore, mockFireAuth, "gid1", "Group1", "example");

    }

    @Test
    public void getGroup_invalid_code() {
        assert !gm.isAvailable();
        assertNull(GroupManagement.getGroup("gidInvalid"));
    }

    @Test
    public void getGroupIDFromUserID() {
        assertEquals("gid1", GroupManagement.getGroupIDFromUserID("example"));
    }

    @Test
    public void getCurrentGroup() {
        assertEquals("gid1", GroupManagement.getCurrentGroup());
    }

    @Test
    public void addUserToExistingGroup() {
        GroupManagement.addUserToGroup("gid1","example2","gc11", "group1");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("example");
        expected.add("example11");
        expected.add("example2");

        assertEquals(expected, GroupManagement.getGroupMemberList("gid1"));

        // check if the group code is added
        assertEquals("gid1", GroupManagement.getGroup("gc11"));
    }

    @Test
    public void addExistingUserToGroup() {
        GroupManagement.addUserToGroup("gid1","example2","gc11", "group1");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("example");
        expected.add("example11");
        expected.add("example2");

        assertEquals(expected, GroupManagement.getGroupMemberList("gid1"));
        // check if the group code is added
        assertEquals("gid1", GroupManagement.getGroup("gc11"));

        GroupManagement.addUserToGroup("gid1","example2","gc12", "group1");
        assertEquals(expected, GroupManagement.getGroupMemberList("gid1"));
        assertNull(GroupManagement.getGroup("gc12"));
    }

    @Test
    public void addUserToNewGroup() {
        GroupManagement.addUserToGroup("gidNew","example2","gc11", "group1");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("example2");

        assertEquals(expected, GroupManagement.getGroupMemberList("gidNew"));

        // check if the group code is added
        assertEquals("gidNew", GroupManagement.getGroup("gc11"));
        assertNull(GroupManagement.getGroup("gc12"));
    }

    @Test
    public void checkGetCode() {
        assertNotNull(GroupManagement.getCode());
    }

    @Test
    public void getGroupMemberList() {
        GroupManagement.addUserToGroup("gidNew","user1","gc1", "groupNew");
        GroupManagement.addUserToGroup("gidNew","user2","gc2", "groupNew");
        GroupManagement.addUserToGroup("gidNew","user3","gc3", "groupNew");

        ArrayList<String> expected = new ArrayList<>();
        expected.add("user1");
        expected.add("user2");
        expected.add("user3");

        assertEquals(expected, GroupManagement.getGroupMemberList("gidNew"));
    }

    @Test
    public void getGroupName() {
        assertEquals(GroupManagement.getGroupName("gid1"), "group1");
        assertNotEquals(GroupManagement.getGroupName("gid2"), "group1");
        assertNull(GroupManagement.getGroupName("gidInvalid"));
    }
//
    @Test
    public void getGroupTask() {
        assert !GroupManagement.getGroupTask("gid1");
        GroupManagement.setGroupTask("gid1", true);
        assert GroupManagement.getGroupTask("gid1");
        GroupManagement.setGroupTask("gid1", false);
        assert !GroupManagement.getGroupTask("gid1");
        assert !GroupManagement.getGroupTask("gid2");
    }

    @Test
    public void setGroupTask() {
        GroupManagement.setGroupTask("gid1", new Boolean(true));
        assert GroupManagement.getGroupTask("gid1");
        GroupManagement.setGroupTask("gid1", new Boolean(false));
        assert !GroupManagement.getGroupTask("gid1");
        GroupManagement.setGroupTask("gid1", true);
        assert GroupManagement.getGroupTask("gid1");
        GroupManagement.setGroupTask("gid1", false);
        assert !GroupManagement.getGroupTask("gid1");
    }

    @Test
    public void removeUserFromGroup() {
        GroupManagement.addUserToGroup("gidNew","user1","gc1", "groupNew");
        GroupManagement.addUserToGroup("gidNew","user2","gc2", "groupNew");
        GroupManagement.addUserToGroup("gidNew","user3","gc3", "groupNew");

        ArrayList<String> expected = new ArrayList<>();
        expected.add("user1");
        expected.add("user2");
        expected.add("user3");

        assertEquals(expected, GroupManagement.getGroupMemberList("gidNew"));

        GroupManagement.removeUserFromGroup("gidNew","user2");
        expected.remove(1);
        assertEquals(expected, GroupManagement.getGroupMemberList("gidNew"));
        GroupManagement.removeUserFromGroup("gidNew","user3");
        expected.remove(1);
        assertEquals(expected, GroupManagement.getGroupMemberList("gidNew"));
        GroupManagement.removeUserFromGroup("gidNew","user1");
        expected.remove(0);
        assertNull(GroupManagement.getGroupMemberList("gidNew"));
    }

    @Test
    public void addGroupCodes() {
        GroupManagement.addGroupCodes("gid1","gc_test_1");
        GroupManagement.addGroupCodes("gid2","gc_test_2");
        assertEquals(GroupManagement.getGroup("gc_test_1"), "gid1");
        assertEquals(GroupManagement.getGroup("gc1"), "gid1");
        assertNotEquals(GroupManagement.getGroup("gc_test_1"), "gid2");
        assertEquals(GroupManagement.getGroup("gc_test_2"), "gid2");
        assertEquals(GroupManagement.getGroup("gc4"), "gid1");
        assertNotEquals(GroupManagement.getGroup("gc3"), "gid1");
    }

    @Test
    public void testGetGroupCode(){
        ArrayList<String> probable = new ArrayList<>();
        probable.add("gc1");
        probable.add("gc4");
        assert probable.contains(GroupManagement.getGroupCode("gid1"));
    }

    @Test
    public void setWeekofYear(){
        List<String> lst1 = new ArrayList<>();
        lst1.add("false");
        lst1.add("GroupName:group1");
        lst1.add("WeekofYear:"+"currweek");
        lst1.add("example");
        lst1.add("example11");

        List<String> lst2 = new ArrayList<>();
        lst2.add("false");
        lst2.add("GroupName:group2");
        lst2.add("WeekofYear:"+"18");
        lst2.add("test");

        Map<String, List<String>> map = new HashMap<>();
        map.put("gid1", lst1);
        map.put("gid2", lst2);

        GroupManagement.Group grp = new GroupManagement.Group(map);
        ArgumentCaptor<GroupManagement.Group> args = ArgumentCaptor.forClass(GroupManagement.Group.class);
        GroupManagement.setWeekofYear("gid1", "currweek");
        verify(mockDocs, times(9)).set(args.capture());

        assert args.getValue().getGrpMap().equals(map);
    }
}