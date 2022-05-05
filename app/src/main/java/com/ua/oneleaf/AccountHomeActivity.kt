package com.ua.oneleaf

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.account_home.*

class AccountHomeActivity : AppCompatActivity() {
    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth;
    private val ref = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_home)

        //Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)


        auth = FirebaseAuth.getInstance()
        checkUser()

        //DataDonuts
        databtn.setOnClickListener {
            showData()
        }

        //Logout
        logoutbtn.setOnClickListener {
            auth.signOut()
            checkUser()
        }
    }

    private fun showData() {
        val intent = Intent(
            this, DataActivity::class.java
        )
        startActivity(intent)
    }

    private fun showHome() {
        val intent = Intent(
            this, MainActivity::class.java
        )
        startActivity(intent)
    }

    private fun checkUser() {
        // If user is already logged in go to profile activity
        // Get current user
        val firebaseUser = auth.currentUser
        val intent = Intent(
            this, MainActivity::class.java
        )
        if (firebaseUser != null) {
            //User is already logged in
            val emailInf = firebaseUser.email
            val tv1: TextView = findViewById(R.id.loggedmail)
            tv1.text = emailInf
        }
        else{
            startActivity(intent)
            finish()
        }
    }


}