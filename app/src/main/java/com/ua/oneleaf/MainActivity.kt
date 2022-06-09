package com.ua.oneleaf

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ua.oneleaf.DataActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth;
    private val ref = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)


        auth = FirebaseAuth.getInstance()
        checkUser()

        val testButtonData = findViewById<Button>(R.id.testData)
        testButtonData.setOnClickListener{
            showData()
        }

        loginHomeBtn.setOnClickListener{
            showLogin()
        }

        registerHomeBtn.setOnClickListener {
            showRegister()
        }
    }

    private fun showLogin() {
        val intent = Intent(
            this, LoginActivity::class.java
        )
        startActivity(intent)
    }

    private fun showRegister() {
        val intent = Intent(
            this, RegisterActivity::class.java
        )
        startActivity(intent)
    }

    private fun showData() {
        val intent = Intent(
            this, DataActivity::class.java
        )
        startActivity(intent)
    }
    private fun checkUser() {
        // If user is already logged in go to profile activity
        // Get current user
        val firebaseUser = auth.currentUser
        val intent = Intent(
            this, AccountHomeActivity::class.java
        )
        if (firebaseUser != null) {
        //User is already logged in
        startActivity(intent)
        finish()
        }
    }
}



