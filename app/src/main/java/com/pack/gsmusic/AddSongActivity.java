package com.pack.gsmusic;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import androidx.activity.EdgeToEdge;
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

    private void initView() {

        TextInputEditText videoUrlET = findViewById(R.id.videoUrlET);
        Button saveVideoBT = findViewById(R.id.videoSaveBT);


        saveVideoBT.setOnClickListener(view -> {
            String videoUrl = videoUrlET.getText().toString().trim();

            if (TextUtils.isEmpty(videoUrl)) {
                Toast.makeText(this, "Enter the video url", Toast.LENGTH_SHORT).show();
            } else {
                saveUrlToDB(videoUrl);
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
}