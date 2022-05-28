package com.ua.oneleaf

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.account_home.*

class AccountHomeActivity : AppCompatActivity() {
    //Progress Dialog

    private lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth;
    private lateinit var storageReference: StorageReference
    private lateinit var binding : AccountHomeActivity
    private lateinit var user: User
    private lateinit var databaseReference: DatabaseReference
    private lateinit var uid: String
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

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

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
                } else {
                    startActivity(intent)
                    finish()
                }

    }

    private fun getUserData(){
        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
        }
    }

}