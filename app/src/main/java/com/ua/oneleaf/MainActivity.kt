package com.ua.oneleaf

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.user_registration.*


class MainActivity : AppCompatActivity() {

    lateinit var handler: DataBaseHelper //linha nova (H)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //codigo novo (H)fsfsdf
        handler = DataBaseHelper(this)

        showHome()

        showRegistration.setOnClickListener{
            showRegistration()
        }

        login.setOnClickListener{
            showLogIn()
        }

        save.setOnCLickListener{
            handler.insertUserData(name.text.toString(), email.text.toString, password_register.text.toString())
        }

        login_button.setOnClickListener{
            handler.userPresent()
        }

    }

    private fun showRegistration(){
        registration_layout.visibility= View.VISIBLE;
        login_layout.visibility=View.GONE
        home.visibility=View.GONE
}

    private fun showLogIn(){
        registration_layout.visibility = View.GONE
        login_layout.visibility = View.VISIBLE
        home.visibility = View.GONE
    }

    private fun showHome(){
        registration_layout.visibility = View.GONE
        login_layout.visibility = View.GONE
        home.visibility = View.VISIBLE
    }



