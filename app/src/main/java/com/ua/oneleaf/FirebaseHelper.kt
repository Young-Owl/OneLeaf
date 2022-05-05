package com.ua.oneleaf

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text


class FirebaseHelper : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val ref = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val email = findViewById<EditText>(R.id.email_register)
        val password = findViewById<EditText>(R.id.password_register)
        val registerBtn = findViewById<Button>(R.id.save)

        registerBtn.setOnClickListener{
            ref.createUserWithEmailAndPassword(
                email.text.toString().trim(),
                password.text.toString().trim()
            )
        }

    }

}