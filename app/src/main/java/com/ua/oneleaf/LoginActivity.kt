package com.ua.oneleaf

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.login.*
import com.ua.oneleaf.DataActivity as DataActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.user_registration.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private val ref = FirebaseAuth.getInstance()

    private var emailVal = ""
    private var passwordVal = ""

    //private val email = findViewById<EditText>(R.id.username)
    //private val password = findViewById<EditText>(R.id.password)
    //private val loginBtn = findViewById<Button>(R.id.loginbtn)

    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        //Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)
        //checkUser()

        // Initialize Firebase Auth
        auth = Firebase.auth

        auth = FirebaseAuth.getInstance()

        loginbtn.setOnClickListener {
            validateData()
        }

        //loginBtn.setOnClickListener {
            //ref.createUserWithEmailAndPassword(
                //email.text.toString().trim(),
                //password.text.toString().trim()
            //)
            //showData()
        //}



        registerfromlogin.setOnClickListener {
            showRegister()
        }

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

    private fun validateData(){
        emailVal = username.text.toString().trim()
        passwordVal = password.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(emailVal).matches()){
            //Invalid email format
            username.error="Invalid email format"
        }
        else if (TextUtils.isEmpty(passwordVal)){
            //no password entered
            password.error="Please enter password"}
        else {
            // Data is validated, Begin login
            FirebaseLogin()
        }
    }

    private fun FirebaseLogin() {
        progressDialog.show()
        auth.signInWithEmailAndPassword(emailVal, passwordVal)
            .addOnSuccessListener{
                //Login Success!
                progressDialog.dismiss()
                //Get User Info
                val firebaseUser = auth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"Logged In as $email", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,DataActivity::class.java))
                finish()
            }
            .addOnFailureListener{ e->
                //Login Failed
                progressDialog.dismiss()
                Toast.makeText(this,"Login Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    //private fun checkUser() {
        // If user is already logged in go to profile activity
        // Get current user
        //val firebaseUser = auth.currentUser
        //val intent = Intent(
            //this, DataActivity::class.java
        //)
        //if (firebaseUser != null) {
            //User is already logged in
            //startActivity(intent)
            //finish()
        //}
    //}
}
