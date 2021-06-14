package com.example.notebook.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook.R;
import com.example.notebook.model.notesModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingleNoteActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference root;
    private FirebaseUser currentFirebaseUser;
    private TextView mtitle;
    private TextView mdescription;
    private FloatingActionButton msubmitbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_note);

        mtitle = (TextView)findViewById(R.id.sntitle);
        mdescription = (TextView)findViewById(R.id.sndescription);
        msubmitbtn = (FloatingActionButton) findViewById(R.id.submitBtn);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        String userId = currentFirebaseUser.getUid();

        final String key;
        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");

        root = db.getReference().child("users").child(userId).child(key);


        root.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                    {
                            String title = dataSnapshot.child("title").getValue(String.class);
                            String description = dataSnapshot.child("description").getValue(String.class);
                            mtitle.setText(title);
                            mdescription.setText(description);
                       }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Note doesn't exist...", Toast.LENGTH_SHORT).show();
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                //throw databaseError.toException();
                Toast.makeText(SingleNoteActivity.this,databaseError.toException().toString(),Toast.LENGTH_LONG).show();
            }
        });


        msubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mtitle.getText().toString();
                String description = mdescription.getText().toString();
                updateNotes(title, description, userId);
            }
        });
    }

    private void updateNotes(String title, String description, String userId) {
        notesModel model = new notesModel(title, description);

        root.setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(SingleNoteActivity.this, HomeActivity.class);
                Toast.makeText(SingleNoteActivity.this, "Notes successfully updated...", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SingleNoteActivity.this, "Please try again...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}