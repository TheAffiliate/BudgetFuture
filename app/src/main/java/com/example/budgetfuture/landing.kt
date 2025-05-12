package com.example.budgetfuture

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Landing : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing)

        // Find the WELCOME button in the layout
        val welcomeButton: Button = findViewById(R.id.welcomebtn)

        // Set an OnClickListener for the WELCOME button
        welcomeButton.setOnClickListener {
            // Creating an intent to navigate to the Register Activity
            val intent = Intent(this, Register::class.java)
            startActivity(intent) // Start the Register Activity
        }
    }
}
