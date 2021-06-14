package com.example.notebook.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.notebook.R;
import com.example.notebook.adapter.HomeAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.notebook.model.notesModel;

public class HomeActivity extends AppCompatActivity implements HomeAdapter.ListItemClickListener{

    private FloatingActionButton maddNoteBtn;
    RecyclerView recycler_view;
    HomeAdapter my_adapter;
    private FirebaseDatabase db;
    private DatabaseReference root;
    private FirebaseUser currentFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        root = db.getReference().child("users");
        String userId = currentFirebaseUser.getUid();

        maddNoteBtn = (FloatingActionButton)findViewById(R.id.addNoteBtn);
        recycler_view = (RecyclerView)findViewById(R.id.recycler_view_home);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<notesModel> options =
                new FirebaseRecyclerOptions.Builder<notesModel>()
                        .setQuery(root.child(userId), notesModel.class)
                        .build();

        my_adapter = new HomeAdapter(options, this);
        recycler_view.setAdapter(my_adapter);

        maddNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,AddNotesActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        my_adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        my_adapter.stopListening();
    }

    @Override
    public void onListItemClick(DataSnapshot snapshot, int position) {
 //     Toast.makeText(this, "position" +position +"key"+ snapshot.getKey(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,SingleNoteActivity.class);
        intent.putExtra("key",snapshot.getKey());
        startActivity(intent);
    }
}