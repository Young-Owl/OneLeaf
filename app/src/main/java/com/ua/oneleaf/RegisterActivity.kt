package com.ua.oneleaf

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.user_registration.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.app.ProgressDialog
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private val ref = FirebaseAuth.getInstance()

    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    private var emailRVal = ""
    private var passwordRVal = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_registration)

        // configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating account In...")
        progressDialog.setCanceledOnTouchOutside(false)
        //init firebase auth
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Auth
        auth = Firebase.auth

        val emailR = findViewById<TextInputEditText>(R.id.email_register)
        val passwordR = findViewById<TextInputEditText>(R.id.password_register)
        val registerB = findViewById<Button>(R.id.save)



        registerB.setOnClickListener {
            validateData()
        }

        loginfromregister.setOnClickListener{
            showLogin()
        }


    }

    private fun showLogin() {
        val intent = Intent(
            this, LoginActivity::class.java
        )
        startActivity(intent)
    }

    private fun showHome() {
        val intent = Intent(
            this, MainActivity::class.java
        )
        startActivity(intent)
    }

    private fun validateData(){
        //get data
        emailRVal = email_register.text.toString().trim()
        passwordRVal = password_register.text.toString().trim()
            //validate data
            if (!Patterns. EMAIL_ADDRESS.matcher(emailRVal).matches()){
                //Invalid email format
                email_register.error = "Invalid email format"
            }
            else if (TextUtils.isEmpty(passwordRVal)){
                //Password isn't entered
                password_register.error = "Please enter password"
            }
            else if (passwordRVal.length < 6){
                password_register.error = "Password must be at least 6 character long"
            }
            else{
                // Data is valid, continue SignUp
                firebaseSignUp()
            }
    }

    private fun firebaseSignUp() {
        //show progress
        progressDialog.show()
        //create account
        auth.createUserWithEmailAndPassword(emailRVal, passwordRVal)
            .addOnSuccessListener {
                //Signup success
                progressDialog.dismiss()
                //Get current user
                val firebaseUser = auth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Account created with email $email", Toast.LENGTH_SHORT).show()
                //open profile
                startActivity(Intent(this, DataActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                //Signup failed
                progressDialog.dismiss()
                Toast.makeText(this, "SignUp Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
