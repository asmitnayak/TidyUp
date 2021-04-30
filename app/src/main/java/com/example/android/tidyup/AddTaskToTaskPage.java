package com.example.android.tidyup;

import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.view.View.OnClickListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AddTaskToTaskPage extends TaskPage implements View.OnClickListener{
    private static final String TAG = "AddTaskToTaskPage.class";
    private Spinner mPersonSpinner;
    private Spinner mSpinnerRewardPenaltyValue;
    private Spinner mSpinnerPriority;
    private Spinner mSpinnerRepetition;
    private static final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    /*private static Map<String, Map<String, List<String>>> taskListDB; // list of tasks
    private static Map<String, Map<String, List<String>>> taskDB; // will store the actual info of tasks
    private static final String COLLECTIONPATH_TASK = "Task";
    private static final String DOCUMENTPATH_TASKS = "Tasks";
    private static final DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_TASK).document(DOCUMENTPATH_TASKS);
*/

    Map<String, TaskItem> tasks = new HashMap<>();

    private Button addTaskButton;
    private EditText taskName;
    private EditText date;
    private static HashMap<String, Object> userMap = new HashMap<>();
    LinearLayout radioGroupLayout;
    RadioGroup rgroup;
    String userUID;
    String user;

    private List<String> membersList;
    public String getGroup(String str){
        int index = str.lastIndexOf("Group=");
        index = index + 6;
        String buildString = "";
        while(str.charAt(index) != ',' && str.charAt(index) != '}'){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    public String getName(String str){
        int index = str.lastIndexOf("Username=");
        index = index + 9;
        String buildString = "";
        while(str.charAt(index) != ',' && str.charAt(index) != '}'){
            buildString += str.charAt(index);
            index++;
        }
        return buildString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_to_task_page);

        Spinner spinnerRewardPenaltyValue = findViewById(R.id.rewardPenaltyValue);
        ArrayList<String> pointList = new ArrayList<>();
        pointList.add("1");
        pointList.add("2");
        pointList.add("3");
        pointList.add("4");
        pointList.add("5");
        ArrayAdapter<String> pointListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pointList);
        pointListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRewardPenaltyValue.setAdapter(pointListAdapter);
        radioGroupLayout = findViewById(R.id.apRewardButtonGroup);
        membersList = GroupManagement.getGroupMemberList((String) UserManagement.getUserDetails().get("GroupID"));
        rgroup = new RadioGroup(this);
        rgroup.setOrientation(RadioGroup.VERTICAL);
        RadioGroup.LayoutParams r12;
        try {
            for (int i = 0; i < membersList.size(); i++) {
                RadioButton r1 = new RadioButton(this);
                r1.setText(UserManagement.getUserNameFromUID(membersList.get(i)));
                Typeface font = Typeface.createFromAsset(getAssets(), "fonts/ArchitectsDaughter-Regular.ttf");
                r1.setTypeface(font);
                r1.setTextSize(20);
                r1.setTextColor(getApplication().getResources().getColor(R.color.fontAndButtonColor));
                r1.setId(i + 1);
                r1.setOnClickListener(this);
                r12 = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
                rgroup.addView(r1, r12);
            }
            radioGroupLayout.addView(rgroup);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Spinner spinnerRepetition = findViewById(R.id.taskRepetitionValue);
        ArrayList<String> repetitionList = new ArrayList<>();
        repetitionList.add("None");
        repetitionList.add("Daily");
        repetitionList.add("Weekly");
        repetitionList.add("Monthly");
        ArrayAdapter<String> repetitionListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, repetitionList);
        repetitionListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepetition.setAdapter(repetitionListAdapter);

       // mPersonSpinner = spinnerPerson;
        mSpinnerRewardPenaltyValue = spinnerRewardPenaltyValue;
        mSpinnerRepetition = spinnerRepetition;
    }

    public void returnToTaskPage(View view) {

        // access the task name

   //     EditText groupIdIn = (EditText) findViewById(R.id.taskName)
        //    String groupID = groupIdIn.getText().toString();
        try {
            EditText nameIn = (EditText) findViewById(R.id.taskName);
            String name = nameIn.getText().toString();

            if (TextUtils.isEmpty(name)) {
                nameIn.setError("Please Enter a Task Name");
                return;
            }

            //EditText person = (EditText) findViewById(R.id.personAssignedToTask);
            //String personStr = person.getText().toString();

            EditText dueDate = (EditText) findViewById(R.id.taskDueDate);
            String date = dueDate.getText().toString();
            if (TextUtils.isEmpty(date)) {
                dueDate.setError("Please Enter a Date");
                return;
            }
            if (!isValid(date)) {
                dueDate.setError("Please Enter a Date in Format ##/##");
                return;
            }

            String rewardString = mSpinnerRewardPenaltyValue.getSelectedItem().toString();
            int rewardInt = Integer.parseInt(rewardString);

            TaskManagment.addTaskItem(name, user, rewardInt, date, mSpinnerRepetition.getSelectedItem().toString());

            finish();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error!" + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, e.toString());
       }
    }
    static boolean isValid(String date) {
        String regex = "\\d\\d/\\d\\d";
        return date.matches(regex);
    }
    @Override
    public void onClick(View v) {
        try {
            user = UserManagement.getUserNameFromUID(membersList.get((int) ((RadioButton) v).getId() - 1));
        }catch (Exception e){
            Toast.makeText(AddTaskToTaskPage.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
    
}
