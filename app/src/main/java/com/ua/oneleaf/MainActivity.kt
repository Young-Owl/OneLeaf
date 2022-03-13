package com.ua.oneleaf

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection
import com.google.android.material.bottomnavigation.BottomNavigationView



class MainActivity : AppCompatActivity() {

    private val donutProgressView by lazy {findViewById<DonutProgressView>(R.id.light_donut)}
    private fun setupDonut() {
        donutProgressView.cap = 5f
        donutProgressView.masterProgress = 0f
        donutProgressView.gapAngleDegrees = 0f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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



        val section1 = DonutSection(
            name = "section_1",
            color = Color.parseColor("#FB1D32"),
            amount = 1f
        )

        val section2 = DonutSection(
            name = "section_2",
            color = Color.parseColor("#FFB98E"),
            amount = 1f
        )

        donutProgressView.cap = 5f
        donutProgressView.submitData(listOf(section1, section2))

    }
}

