package com.ua.oneleaf

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.registration_account.*
import kotlinx.android.synthetic.main.user_registration.*


class MainActivity : AppCompatActivity() {

    private val donutProgressView by lazy {findViewById<DonutProgressView>(R.id.light_donut)}

    private fun setupDonut() {
        donutProgressView.cap = 100f
        donutProgressView.masterProgress = 0f
        donutProgressView.gapAngleDegrees = 0f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        //codigo novo (H)
        //handler = DataBaseHelper(this)

        //showHome()

        //showRegistration.setOnClickListener{
        //   showRegistration()
        //  }

       // login.setOnClickListener{
        //    showLogIn()
       // }

        //save.setOnCLickListener{
        //    handler.insertUserData(name.text.toString(), email.text.toString(), password_register.text.toString())
         //   showHome()

       // }

     // login_button.setOnClickListener{
      //  if (handler.userPresent(login_email.text.toString(), login_password.text.toString()))
      //         Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
      //      else
      //          Toast.makeText(this,"Username or password is incorrect", Toast.LENGTH_SHORT).show()
    //  }



// para aqui (H)

  // setupDonut()



    // Bottom Nav Menu
    val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
    val navController = navHostFragment.navController//findNavController(R.id.fragment)
    val appBarConfiguration = AppBarConfiguration((setOf(R.id.data_fragment, R.id.account_fragment, R.id.settings_fragment)))

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

//    donutProgressView.cap = 5F
  //  donutProgressView.submitData(listOf(section1, section2, section3))

    }



   //lateinit var handler: DataBaseHelper //linha nova (H)

// Registo e Login

    private fun showRegistration(){
        registration_account.visibility= View.VISIBLE
        user_registration.visibility=View.GONE
        login.visibility=View.GONE
        //activity_main.visibility=View.GONE
    }

    private fun showUserRegister(){
        registration_account.visibility= View.GONE
        user_registration.visibility=View.VISIBLE
        login.visibility=View.GONE
       // activity_main.visibility=View.GONE
    }

    private fun showLogIn(){
        registration_account.visibility= View.GONE
        user_registration.visibility=View.GONE
        login.visibility=View.VISIBLE
       // activity_main.visibility=View.GONE
    }

    private fun showHome(){
        registration_account.visibility= View.GONE
        user_registration.visibility=View.GONE
        login.visibility=View.GONE
       // activity_main.visibility=View.VISIBLE
    }
}


