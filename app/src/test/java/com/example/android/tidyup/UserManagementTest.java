package com.example.android.tidyup;
import android.content.Context;
import android.util.Base64;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserManagementTest {

    private UserManagement um;
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
        ListenerRegistration mockLR = mock(ListenerRegistration.class);
//        Task mockTaskUpdate = mock(Task.class);
//        Task mockTaskSet = mock(Task.class);

        ///////////////////////////////////
        //////   MOCKING FUNCTIONS   //////
        ///////////////////////////////////

        doReturn(mockCollections).when(mockFirestore).collection(anyString());
        doReturn(mockDocs).when(mockCollections).document(anyString());
        doReturn(mockFireUser).when(mockFireAuth).getCurrentUser();
        doReturn("example").when(mockFireUser).getUid();
//        when(mockDocs.update(any(Map.class))).thenReturn(mockTaskUpdate);
//        when(mockDocs.set(any(Object.class))).thenReturn(mockTaskSet);

        /////////////////////////////////
        //////   User Management   //////
        /////////////////////////////////
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, List<String>> othermap = new HashMap<>();

        List<String> lst1 = new ArrayList<String>(){{
            add("EXAMPLE_USER");
            add("12");
            add("example.com");
            add("example token");
        }};

        List<String> lst2 = new ArrayList<String>(){{
            add("EX_USER");
            add("21");
            add("example2.com");
            add("example2 token");
        }};

        map.put("Email", "example.com");
        map.put("Group", "example");
        map.put("GroupID", "e1");
        map.put("Role", "Admin");
        map.put("Task", "example task");
        map.put("Token", "example token");
        map.put("UserPoints", "12");
        map.put("Username", "EXAMPLE_USER");

        othermap.put("docID1", lst1);
        othermap.put("docID2", lst2);

        um = new UserManagement(mockFirestore, mockFireAuth, mockDocs, map, othermap);

        //////////////////////////////////
        //////   Group Management   //////
        //////////////////////////////////

        gm = new GroupManagement(mockFirestore, mockFireAuth, "gid1", "group1", "example");
        GroupManagement.addGroupCodes("gid1","gc1");
        GroupManagement.addGroupCodes("gid2","gc2");
        GroupManagement.addGroupCodes("gid3","gc3");
        GroupManagement.addGroupCodes("gid1","gc4");

        GroupManagement.addUserToGroup("gid1","example2","gc11","group1");
        GroupManagement.addUserToGroup("gid2","test","gc21","group2");
        GroupManagement.addUserToGroup("new_groupid_1","usr1","gc_new_1","group2");

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
    public void addStringField(){
        Map<String, String> data = new HashMap<>();
        data.put("string_add", "");

        Task mockTask = mock(Task.class);
        when(mockDocs.set(any(Map.class), any(SetOptions.class))).thenReturn(mockTask);
        UserManagement.addStringField("string_add");
        verify(mockDocs).set(data, SetOptions.merge());
    }

    @Test
    public void addIntField(){
        Map<String, String> data = new HashMap<>();
        data.put("string_add", "0");

        Task mockTask = mock(Task.class);
        when(mockDocs.set(any(Map.class), any(SetOptions.class))).thenReturn(mockTask);
        UserManagement.addIntField("string_add");
        verify(mockDocs).set(data, SetOptions.merge());
    }

    @Test
    public void deleteField(){
        Map<String, String> data = new HashMap<>();
        data.put("delete_key", null);

        Task mockTask = mock(Task.class);
        when(mockDocs.update(anyString(), any(Object.class))).thenReturn(mockTask);
        UserManagement.deleteField("delete_key");
        verify(mockDocs).update("delete_key", FieldValue.delete());
    }

    @Test
    public void updateUserGroup(){
        Task mockTask = mock(Task.class);
        Context mockContext = mock(Context.class);

        when(mockDocs.update(anyString(), any(Object.class),anyString(), any(Object.class),anyString(), any(Object.class))).thenReturn(mockTask);
        doReturn(mockTask).when(mockTask).addOnSuccessListener(any(OnSuccessListener.class));

        UserManagement.updateUserGroup("new_groupid_1", "Admin", mockContext);
        verify(mockDocs).update("GroupID", "new_groupid_1",
                "Group", GroupManagement.getGroupName("new_groupid_1"),
                "Role", "Admin");
    }

    @Test
    public void getUserDetails(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("Email", "example.com");
        map.put("Group", "example");
        map.put("GroupID", "e1");
        map.put("Role", "Admin");
        map.put("Task", "example task");
        map.put("Token", "example token");
        map.put("UserPoints", "12");
        map.put("Username", "EXAMPLE_USER");

        assertEquals(UserManagement.getUserDetails(), map);
    }

    @Test
    public void getEmailFromUID(){
        assertEquals(UserManagement.getEmailFromUID("docID1"), "example.com");
        assertEquals(UserManagement.getEmailFromUID("docID2"), "example2.com");
        assertNull(UserManagement.getEmailFromUID("docID3"));
    }

    @Test
    public void getUserPointsFromUID(){
        assertEquals(UserManagement.getUserPointsFromUID("docID1"), "12");
        assertEquals(UserManagement.getUserPointsFromUID("docID2"), "21");
        assertNull(UserManagement.getEmailFromUID("docID3"));
    }

    @Test
    public void resetAllUserPoints(){
        UserManagement.resetAllUserPoints("gid1");
        Map<String, String> data = new HashMap<>();
        data.put("UserPoints", "0");

        verify(mockDocs, times(2)).set(data, SetOptions.merge());

        UserManagement.resetAllUserPoints("gid_invalid");
        verify(mockDocs, times(2)).set(data, SetOptions.merge());
    }

    @Test
    public void setUsername(){
        UserManagement.setUsername("newUsername");
        verify(mockDocs).update("Username", "newUsername");
    }

    @Test
    public void setUserEmail(){
        UserManagement.setUserEmail("newEmail");
        verify(mockDocs).update("Email", "newEmail");
    }

    @Test
    public void setUserPassword(){
        UserManagement.setUserPassword("newPassword");
        verify(mockDocs).update("Password", "newPassword");
    }

    @Test
    public void setUserRole(){
        UserManagement.setUserRole("User");
        verify(mockDocs).update("Role", "User");
    }

    @Test
    public void setToken(){
        UserManagement.setToken("newToken");
        verify(mockDocs).update("Token", "newToken");
    }

    @Test
    public void getUserTokenFromUID(){
        assertEquals(UserManagement.getUserTokenFromUID("docID1"), "example token");
        assertEquals(UserManagement.getUserTokenFromUID("docID2"), "example2 token");
        assertNull(UserManagement.getUserTokenFromUID("docID3"));
    }

    @Test
    public void setUserGroupID(){
        UserManagement.setUserGroupID("newgID");
        verify(mockDocs).update("GroupID", "newgID");
    }

    @Test
    public void setUserGroup(){
        UserManagement.setUserGroup("newGroup");
        verify(mockDocs).update("Group", "newGroup");
    }

    @Test
    public void updateUserPoints(){
        UserManagement.updateUserPoints("13");
        verify(mockDocs).update("UserPoints", "25");
    }

    @Test
    public void preExecute(){
        Task mockTask = mock(Task.class);
        ListenerRegistration mockLR = mock(ListenerRegistration.class);
        doReturn(mockLR).when(mockCollections).addSnapshotListener(any(EventListener.class));
        doReturn(mockTask).when(mockCollections).get(any());
        doReturn(mockTask).when(mockTask).addOnCompleteListener(any(OnCompleteListener.class));

        doReturn(mockTask).when(mockDocs).get(any());
//        doReturn(mockLR).when(mockDocs).addSnapshotListener(any(EventListener.class));

        um.onPreExecute();

        assert true;
    }
}
