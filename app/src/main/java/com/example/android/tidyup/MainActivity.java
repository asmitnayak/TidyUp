package com.example.android.tidyup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DocSnippets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fireStoreAdd2Doc();
    }

    private void fireStoreAdd2Doc(){
        C_D ab = new C_D("Hello2", "World2");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("first-time").document("id-specific").set(ab, SetOptions.merge());
    }

    private void fireStoreAddObject(){
        A_B ab = new A_B("Hello", "World");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("first-time").document("id-specific").set(ab, SetOptions.merge());
    }

    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("testing/tested");
    private void fireStoreDBDocHandling(){
        Map<String, Object> entry = new HashMap<>();
        entry.put("Place", "New York");
        mDocRef.set(entry).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Log.d(TAG, "Document added");
                else
                    Log.d(TAG, "Document addition failed");
            }
        });
    }

    private void fireStoreDBHandling(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        // Create a new user with a first, middle, and last name
        user = new HashMap<>();
        user.put("first", "Alan");
        user.put("middle", "Mathison");
        user.put("last", "Turing");
        user.put("born", 1912);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    class A_B{

        String a, b;

        A_B(){};
        A_B(String a, String b){
            this.a = a;
            this.b = b;
        }

        public String getA(){
            return this.a;
        }
        public String getB(){
            return this.b;
        }
    }
    class C_D{

        String a, b;

        C_D(){};
        C_D(String a, String b){
            this.a = a;
            this.b = b;
        }

        public String getC(){
            return this.a;
        }
        public String getD(){
            return this.b;
        }
    }

}