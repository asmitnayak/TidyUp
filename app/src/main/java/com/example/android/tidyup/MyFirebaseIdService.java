package com.example.android.tidyup;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        UpdateToken();
    }
    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Task<String> refreshToken= FirebaseMessaging.getInstance().getToken().continueWithTask(new Continuation<String, Task<String>>() {
            @Override
            public Task<String> then(@NonNull Task<String> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return FirebaseMessaging.getInstance().getToken();
            }
        }).addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    String sToken = task.getResult();
                    Token token= new Token(sToken);
                    UserManagement.setToken(token.getToken());
                }
            }
        });
    }
}