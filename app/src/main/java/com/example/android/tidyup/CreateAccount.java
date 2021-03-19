package com.example.android.tidyup;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CreateAccount extends AppCompatActivity {


    protected static final ArrayList<String> CREDENTIALS = new ArrayList<>();

    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mPasswordConfView;
    private EditText mAddressView;
    private EditText mPhoneNumberView;
    private Spinner mRoleSpinner;
    private UserLoginTask mAuthTask;

    public static String[] accountDetails = new String[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_page);
        Spinner spinner = findViewById(R.id.role);
        ArrayList<String> roleList = new ArrayList<>();
        roleList.add("Admin");
        roleList.add("User");
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roleList);
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(listAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                String tutorial = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });



        mAuthTask = new UserLoginTask();
        mUsernameView = findViewById(R.id.emailInput);
        mPasswordView = findViewById(R.id.passwordInput);
        mPasswordConfView = findViewById(R.id.passwordInputConfirm);
        mAddressView = findViewById(R.id.address);
        mPhoneNumberView = findViewById(R.id.phoneNumber);
        mRoleSpinner = spinner;

        Button mRegisterButton = (Button) findViewById(R.id.registerLink);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mAuthTask.write_credentials())
                        finish();
                    // TODO: go to login this line is error generating
                    if (mRoleSpinner.getSelectedItem().toString().equalsIgnoreCase("Admin")){

                    }
                    else if (mRoleSpinner.getSelectedItem().toString().equalsIgnoreCase("User")) {
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void goToAccountPage(){
        //Intent intent = new Intent(this, View.class);
        //startActivity(intent);
    }



    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private String mUser;
        private String mPassword;

        UserLoginTask(String user, String password) {
            mUser = user;
            mPassword = password;
        }
        UserLoginTask() {
            mUser = "";
            mPassword = "";

        }

        void setUsernamePassword(String user, String pass){
            mUser = user;
            mPassword = pass;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            if(CREDENTIALS.isEmpty())
                return false;

            for (String credential : CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mUser)) {
                    // Account exists, return true if the password matches.
                    if (pieces[1].equals(mPassword))
                        System.arraycopy(pieces, 0, accountDetails, 0, 6);
                    return  pieces[1].equals(mPassword);
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }

        public ArrayList<String> read_credentials() throws IOException {


            return CREDENTIALS;
        }

        private boolean write_credentials() throws IOException {
            boolean success = false;


            return success;
        }
    }

}
