package com.ua.oneleaf

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.logout
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.user_registration.*
import com.ua.oneleaf.DataActivity as DataActivity

class MainActivity : AppCompatActivity() {

    lateinit var handler: DataBaseHelper //linha nova (H)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Codigo Novo(G)
        val testButtonData = findViewById<Button>(R.id.testData)
        testButtonData.setOnClickListener{
            showData()
        }
        //

        //codigo novo (H)
        handler = DataBaseHelper(this)

        showHome()

        registerfromlogin.setOnClickListener {
            showRegistration()
        }

        loginfromregister.setOnClickListener {
            showLogIn()
        }

        registration.setOnClickListener {
            showRegistration()
        }

        logout.setOnClickListener {
            showLogIn()
        }

        save.setOnClickListener {
            handler.insertUserData(name.text.toString(), email.text.toString(), password_register.text.toString())
            showHome()
        }

        loginbtn.setOnClickListener {
            if (handler.userPresent(logout.text.toString(), password.text.toString())) {
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Username or password is incorrect", Toast.LENGTH_SHORT).show()
            }
        }

        testData.setOnClickListener {
            showData()
        }

    }

    private fun showRegistration() {
        registration_layout.visibility = View.VISIBLE;
        login_layout.visibility = View.GONE
        home_ll.visibility = View.GONE
        data_layout.visibility = View.GONE
    }
//
    private fun showLogIn() {
        registration_layout.visibility = View.GONE
        login_layout.visibility = View.VISIBLE
        home_ll.visibility = View.GONE
        data_layout.visibility = View.GONE
    }

    private fun showHome() {
        registration_layout.visibility = View.GONE
        login_layout.visibility = View.GONE
        home_ll.visibility = View.VISIBLE
        data_layout.visibility = View.GONE
    }

    // Código Novo (G)
    private fun showData() {
        val intent = Intent(
            this, DataActivity::class.java
        )
        startActivity(intent)
    }
    // Acaba Aqui
}



