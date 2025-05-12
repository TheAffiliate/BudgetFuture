package com.example.budgetfuture

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Register : AppCompatActivity() {
    //suppressing the access warning
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nameEditText: EditText = findViewById(R.id.username)
        val emailEditText: EditText = findViewById(R.id.EmailAddress)
        val passwordEditText: EditText = findViewById(R.id.Password)
        val confirmPasswordEditText: EditText = findViewById(R.id.confirmPassword)
        val registerButton: Button = findViewById(R.id.registerbtn)

        // Register Button Click
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            // validating the  email
            if (!email.contains('@')) {
                emailEditText.error = "Email must contain '@'"
                return@setOnClickListener
            }

            // Validate password match
            if (password != confirmPassword) {
                confirmPasswordEditText.error = "Passwords do not match"
                return@setOnClickListener
            }

            // Validate if password is not empty
            if (password.isEmpty()) {
                passwordEditText.error = "Password cannot be empty"
                return@setOnClickListener
            }


            //saving the credentials of the user
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("name", name)
            editor.putString("email", email)
            editor.putString("password", password)
            editor.apply()

            // Successful registration
            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()


            // Navigate to the Login activity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}
