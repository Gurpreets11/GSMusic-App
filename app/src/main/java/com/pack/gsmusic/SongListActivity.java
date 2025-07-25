package com.pack.gsmusic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pack.gsmusic.adapter.UserAdapter;
import com.pack.gsmusic.dto.User;
import com.pack.gsmusic.dto.Video;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SongListActivity extends AppCompatActivity {


    RecyclerView userRecyclerView;
    UserAdapter userAdapter;
    List<User> userList;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_song_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        initView();
    }

    private void initView() {


        Button addSongsBT = findViewById(R.id.addSongsBT);
        addSongsBT.setOnClickListener(view->{
            Intent intent = new Intent(SongListActivity.this, AddSongActivity.class);
            startActivity(intent);
        });


        userRecyclerView = findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList);
        userRecyclerView.setAdapter(userAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        loadUsers();
    }


    private void loadUsers() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear(); // clear old data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SongListActivity.this, "Failed to load users.", Toast.LENGTH_SHORT).show();
                Log.e("Firebase", "Error: " + error.getMessage());
            }
        });
    }
    public void readUserData() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    Long age = snapshot.child("age").getValue(Long.class);

                    Log.d("Firebase", "Name: " + name + ", Email: " + email + ", Age: " + age);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });
    }


    public void loadVideosByPlaylist(String playlistId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("videos");

        ref.orderByChild("playlistId").equalTo(playlistId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Video> videoList = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Video video = ds.getValue(Video.class);
                            videoList.add(video);
                        }

                        // Pass to RecyclerView adapter here
                        Log.d("VIDEOS", "Total videos: " + videoList.size());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FIREBASE", error.getMessage());
                    }
                });
    }

}