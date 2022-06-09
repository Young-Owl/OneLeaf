package com.ua.oneleaf

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.account_home.*
import kotlinx.android.synthetic.main.account_home.refreshAccount
import kotlinx.android.synthetic.main.data_donuts.*
import java.io.File

class AccountHomeActivity : AppCompatActivity() {
    //Progress Dialog

    private lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth;
    private lateinit var storageReference: StorageReference
    private lateinit var binding : AccountHomeActivity
    private lateinit var user: com.ua.oneleaf.User
    private lateinit var databaseReference: DatabaseReference
    private lateinit var uid: String
    private val ref = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_home)


        //Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Getting Data...")
        progressDialog.setCanceledOnTouchOutside(false)

        auth = FirebaseAuth.getInstance()

        uid = auth.currentUser?.uid.toString()

        progressDialog.show()
        checkUser()

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        getUserData()
        getProfilePic()


        //DataDonuts
        databtn.setOnClickListener {
            showData()
        }
        //Add Vase
        vasebtn.setOnClickListener{
            showVases()
        }

        profile_image.setOnClickListener{
           showUpdatePFP()
        }

        //Logout
        logoutbtn.setOnClickListener {
            auth.signOut()
            checkUser()
        }
        refreshApp()
    }

    private fun showData() {
        val intent = Intent(
            this, DataActivity::class.java
        )
        startActivity(intent)
    }

    private fun showUpdatePFP() {
        val intent = Intent(
            this, ProfileUpdate::class.java
        )
        startActivity(intent)
    }

    private fun showVases() {
        val intent = Intent(
            this, VaseRegister::class.java
        )
        startActivity(intent)
    }

    private fun checkUser() {
        progressDialog.show()
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
        progressDialog.show()
        databaseReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user = snapshot.child("$uid/Data").getValue(com.ua.oneleaf.User::class.java)!!
                loggedmail.text = user.username
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })
    }

    private fun getProfilePic(){
        storageReference = FirebaseStorage.getInstance().reference.child("Users/$uid"+".png")
        val localFile = File.createTempFile("tempImage", "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            profile_image.setImageBitmap(bitmap)
            progressDialog.dismiss()

        }.addOnFailureListener{
            Toast.makeText(this, "Failed to retrieve PFP", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }
        progressDialog.dismiss()
    }

    private fun refreshApp() {
        progressDialog.show()
        refreshAccount.setOnRefreshListener {
            // Start

            getUserData()
            getProfilePic()
            // End

            Toast.makeText(this, "Page Refreshed!", Toast.LENGTH_SHORT).show()
            refreshAccount.isRefreshing = false
            progressDialog.dismiss()
        }
    }
}