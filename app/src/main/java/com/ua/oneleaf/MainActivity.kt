package com.ua.oneleaf

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.loginHomeBtn
import com.ua.oneleaf.DataActivity as DataActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth;
    private val ref = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        //save.setOnClickListener {
            //handler.insertUserData(email_register.text.toString(), password_register.text.toString())
            //showHome()
        //}

        //loginbtn.setOnClickListener {
            //if (handler.userPresent(logout.text.toString(), password.text.toString())) {
                //Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
            //}
            //else {
                //Toast.makeText(this, "Username or password is incorrect", Toast.LENGTH_SHORT).show()
            //}
        //}
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
}



