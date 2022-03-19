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
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private val donutProgressView by lazy {findViewById<DonutProgressView>(R.id.light_donut)}
    private fun setupDonut() {
        donutProgressView.cap = 100f
        donutProgressView.masterProgress = 0f
        donutProgressView.gapAngleDegrees = 0f
    }

    lateinit var handler:DataBaseHelper //linha nova (H)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //codigo novo (H)
        handler = DataBaseHelper(this)

        showHome()

        showRegistration.setOnClickListener{
            showRegistration()
        }

        login.setOnClickListener{
            showLogIn()
        }

        save.setOnCLickListener{
            handler.insertUserData(name.text.toString())
        }

    }

// para aqui (H)

    setupDonut()



        // Bottom Nav Menu
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)
        //val appBarConfiguration = AppBarConfiguration((setOf(R.id.info_fragment, R.id.account_fragment, R.id.settings_fragment)))

        //setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)


        // Fullscreen App (No Status bar)
        //window.setFlags(
        //WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Fullscreen App (No Status and Task bar)
        //window.decorView.apply {
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher
            //systemUiVisibility =
                //View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        //}

        var masterprogress = 40F
        var amount1 = 0F
        var amount2 = 0F
        var amount3 = 0F


        if(masterprogress <= 33F){
            amount1 = masterprogress
            amount2 = 0F
            amount3 = 0F
        }
        else if(masterprogress in 33F..66F){
            amount1 = 33F
            amount2 = masterprogress - 33F
            amount3 = 0F
        }
        else if(masterprogress in 66F..100F){
            amount1 = 33F
            amount2 = 33F
            amount3 = 66F - masterprogress
        }

        val section1 = DonutSection(
            name = "section_1",
            color = Color.parseColor("#FB1D32"),
            amount = amount1
        )

        val section2 = DonutSection(
            name = "section_2",
            color = Color.parseColor("#FFB98E"),
            amount = amount2
        )

        val section3 = DonutSection(
            name = "section_3",
            color = Color.parseColor("#FFB98E"),
            amount = amount3
        )

        donutProgressView.cap = 5F
        donutProgressView.submitData(listOf(section1, section2, section3))

    }


// Registo e Login

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



