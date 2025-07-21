package com.pack.gsmusic

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


import android.content.Intent
 import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val register: Button = findViewById(R.id.register)
        val login: Button = findViewById(R.id.login)

        register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}

// https://github.com/firebase/quickstart-android/tree/master/database