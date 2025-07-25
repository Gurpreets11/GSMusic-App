package com.pack.gsmusic;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pack.gsmusic.dto.PlaylistCategory;
import com.pack.gsmusic.dto.Video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddSongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_song);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();

    }
    private EditText edtTitle;
    private Spinner spinnerPlaylist;


    private List<PlaylistCategory> playlistList = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;
    private String selectedPlaylistId;
    private void initView() {

        TextInputEditText videoUrlET = findViewById(R.id.videoUrlET);
        Button saveVideoBT = findViewById(R.id.videoSaveBT);
        edtTitle = findViewById(R.id.edtTitle);
         spinnerPlaylist = findViewById(R.id.spinnerPlaylist);

        loadPlaylists();
        saveVideoBT.setOnClickListener(view -> {
            String videoUrl = videoUrlET.getText().toString().trim();
            //String title = edtTitle.getText().toString().trim();
            //String url = edtUrl.getText().toString().trim();

            /*if (title.isEmpty() || url.isEmpty() || selectedPlaylistId == null) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }*/
            if (TextUtils.isEmpty(videoUrl) || selectedPlaylistId == null) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                saveUrlToDB(videoUrl);
                saveVideo("", videoUrl, selectedPlaylistId);
            }
        });
    }

    private void saveUrlToDB(String videoUrl) {
        Log.e("TAG", "videoUrl :" + videoUrl);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user");

        String userId = userRef.push().getKey();  // generate unique ID
        HashMap<String, Object> user = new HashMap<>();
        user.put("name", "Gurpreet");
        user.put("email", "gurpreet@example.com");
        user.put("age", 28);

        userRef.child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });


    }


    private void loadPlaylists() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("playlist_categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                playlistList.clear();
                List<String> names = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    PlaylistCategory cat = ds.getValue(PlaylistCategory.class);
                    playlistList.add(cat);
                    names.add(cat.name);
                }

                spinnerAdapter = new ArrayAdapter<>(AddSongActivity.this,
                        android.R.layout.simple_spinner_item, names);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerPlaylist.setAdapter(spinnerAdapter);

                spinnerPlaylist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedPlaylistId = playlistList.get(position).id;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedPlaylistId = null;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddSongActivity.this, "Failed to load playlists", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveVideo(String title, String url, String playlistId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("videos");
        String videoId = ref.push().getKey();
        Video video = new Video(videoId, title, url, playlistId);

        ref.child(videoId).setValue(video)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Video saved", Toast.LENGTH_SHORT).show();
                    edtTitle.setText("");

                    spinnerPlaylist.setSelection(0);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
    public void savePlaylistCategories() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("playlist_categories");

        String id1 = ref.push().getKey();
        PlaylistCategory cat1 = new PlaylistCategory(id1, "Punjabi Songs");
        ref.child(id1).setValue(cat1);

        String id2 = ref.push().getKey();
        PlaylistCategory cat2 = new PlaylistCategory(id2, "Hindi Songs");
        ref.child(id2).setValue(cat2);

        String id3 = ref.push().getKey();
        PlaylistCategory cat3 = new PlaylistCategory(id3, "Sufi");
        ref.child(id3).setValue(cat3);
    }

}