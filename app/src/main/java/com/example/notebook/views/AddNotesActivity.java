package com.example.notebook.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notebook.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.notebook.model.notesModel;

import java.security.Timestamp;
import java.util.Calendar;
import java.util.Date;


public class AddNotesActivity extends AppCompatActivity {

    private EditText mtitle, mdescription;
    private FloatingActionButton msubmitbtn;
    private FirebaseDatabase db;
    private DatabaseReference root;
    private FirebaseUser currentFirebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        mtitle = (EditText) findViewById(R.id.sntitle);
        mdescription = (EditText) findViewById(R.id.sndescription);
        msubmitbtn = (FloatingActionButton) findViewById(R.id.submitBtn);
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        root = db.getReference().child("users");
        String userId = currentFirebaseUser.getUid();


        msubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mtitle.getText().toString();
                String description = mdescription.getText().toString();
                addNotes(title, description, userId);
            }
        });
    }

    private void addNotes(String title, String description, String userId) {
        notesModel model = new notesModel(title, description);
        String key =  root.push().getKey();

        root.child(userId).child(key).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(AddNotesActivity.this, HomeActivity.class);
                Toast.makeText(AddNotesActivity.this, "Notes successfully uploaded...", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNotesActivity.this, "Please try again...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}