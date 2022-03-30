package com.ua.oneleaf

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.login
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.user_registration.*


class MainActivity : AppCompatActivity() {

    lateinit var handler: DataBaseHelper //linha nova (H)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //codigo novo (H)
        handler = DataBaseHelper(this)

        showHome()

        registration.setOnClickListener {
            showRegistration()
        }

        login.setOnClickListener {
            showLogIn()
        }

        save.setOnClickListener {
            handler.insertUserData(name.text.toString(), email.text.toString(), password_register.text.toString())
            showHome()
        }

        loginbtn.setOnClickListener {
            if (handler.userPresent(login.text.toString(), password.text.toString()))
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "Username or password is incorrect", Toast.LENGTH_SHORT).show()
        }

    }

    private fun showRegistration() {
        registration_layout.visibility = View.VISIBLE;
        login_layout.visibility = View.GONE
        home_ll.visibility = View.GONE
    }

    private fun showLogIn() {
        registration_layout.visibility = View.GONE
        login_layout.visibility = View.VISIBLE
        home_ll.visibility = View.GONE
    }

    private fun showHome() {
        registration_layout.visibility = View.GONE
        login_layout.visibility = View.GONE
        home_ll.visibility = View.VISIBLE
    }
}



