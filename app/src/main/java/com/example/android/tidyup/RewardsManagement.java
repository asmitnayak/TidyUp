package com.example.android.tidyup;

import android.os.AsyncTask;

import com.google.firebase.firestore.FirebaseFirestore;

public class RewardsManagement extends AsyncTask<Void, Void, Void> {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
