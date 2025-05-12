package com.example.budgetfuture

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Fetching the user interface elements
        val emailEditText: EditText = findViewById(R.id.EmailAddress1)
        val passwordEditText: EditText = findViewById(R.id.PasswordLogin)
        val loginButton: Button = findViewById(R.id.loginbtn)

        // Set OnClickListener for the Login button
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validate if email  contain '@'
            if (!email.contains('@')) {
                emailEditText.error = "Email must contain '@'"
                return@setOnClickListener
            }

            // Validate password to ensure it's not empty
            if (password.isEmpty()) {
                passwordEditText.error = "Password cannot be empty"
                return@setOnClickListener
            }

            // Retrieve saved email and password from SharedPreferences
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val savedEmail = sharedPreferences.getString("email", "")
            val savedPassword = sharedPreferences.getString("password", "")

            // Check if entered credentials match saved ones
            if (email == savedEmail && password == savedPassword) {
                // If login validation passes, show a success message
                Toast.makeText(this, "Login Successful!!!,Welcome to the Cap Tracker app!!!", Toast.LENGTH_SHORT).show()

                // Navigate to the Dashboard activity after successful login
                val intent = Intent(this, Dashboard::class.java)
                startActivity(intent)

                // finish the login activity so the user cannot navigate back
                finish()
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
