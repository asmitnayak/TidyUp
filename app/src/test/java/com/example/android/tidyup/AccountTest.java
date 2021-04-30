//package com.example.android.tidyup;
//
//import android.app.Activity;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.ListenerRegistration;
//
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import androidx.appcompat.app.AppCompatDelegate;
//import androidx.lifecycle.ReportFragment;
//import androidx.test.core.app.ActivityScenario;
//
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.powermock.api.mockito.PowerMockito.doNothing;
//import static org.powermock.api.mockito.PowerMockito.doReturn;
//import static org.powermock.api.mockito.PowerMockito.mock;
//import static org.powermock.api.mockito.PowerMockito.mockStatic;
//import static org.powermock.api.mockito.PowerMockito.spy;
//
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({ ReportFragment.class })
//public class AccountTest {
//
//    private FirebaseFirestore mockFirestore;
//    private FirebaseAuth mockFireAuth;
//    private FirebaseUser mockFireUser;
//    private CollectionReference mockCollections;
//    private DocumentReference mockDocs;
//
//    @Before
//    public void setUp() {
//
//        /////////////////////////////////
//        //////   MOCKING CLASSES   //////
//        /////////////////////////////////
//
//        mockFirestore = Mockito.mock(FirebaseFirestore.class);
//        mockFireAuth = Mockito.mock(FirebaseAuth.class);
//        mockFireUser = Mockito.mock(FirebaseUser.class);
//        mockCollections = Mockito.mock(CollectionReference.class);
//        mockDocs = Mockito.mock(DocumentReference.class);
//        ListenerRegistration mockLR = Mockito.mock(ListenerRegistration.class);
////        Task mockTaskUpdate = mock(Task.class);
////        Task mockTaskSet = mock(Task.class);
//
//        ///////////////////////////////////
//        //////   MOCKING FUNCTIONS   //////
//        ///////////////////////////////////
//
//        Mockito.doReturn(mockCollections).when(mockFirestore).collection(anyString());
//        Mockito.doReturn(mockDocs).when(mockCollections).document(anyString());
//        Mockito.doReturn(mockFireUser).when(mockFireAuth).getCurrentUser();
//        Mockito.doReturn("example").when(mockFireUser).getUid();
////        when(mockDocs.update(any(Map.class))).thenReturn(mockTaskUpdate);
////        when(mockDocs.set(any(Object.class))).thenReturn(mockTaskSet);
//
//    }
//
//    @Test
//    public void onCreate(){
//        mockStatic(ReportFragment.class);
//
//        Account activity = spy(new Account(mockFireAuth, mockFireUser, mockFirestore));
//
//        doNothing().when(activity).setContentView(any());
//        doReturn(mock(AppCompatDelegate.class)).when(activity).getDelegate();
//
//
//        activity.onCreate(null);
//
//    }
//}
