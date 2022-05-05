package com.ua.oneleaf

import android.accounts.Account
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
    //Progress Dialog

    private lateinit var auth: FirebaseAuth;
    private val ref = FirebaseAuth.getInstance()

    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog


    private var emailVal = ""
    private var passwordVal = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Login In...")
        progressDialog.setCanceledOnTouchOutside(false)

        // Initialize Firebase Auth
        auth = Firebase.auth

        auth = FirebaseAuth.getInstance()

        loginbtn.setOnClickListener {
            validateData()
        }

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
                startActivity(Intent(this,AccountHomeActivity::class.java))
                finish()
            }
            .addOnFailureListener{ e->
                //Login Failed
                progressDialog.dismiss()
                Toast.makeText(this,"Login Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
}
