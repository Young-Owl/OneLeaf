package com.ua.oneleaf

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.account_home.*
import kotlinx.android.synthetic.main.data_donuts.*
import kotlinx.coroutines.delay
import kotlin.properties.Delegates
import kotlin.random.Random

class DataActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth;
    private lateinit var databaseReference: DatabaseReference
    private lateinit var uid: String

    private lateinit var vaseID: String
    private lateinit var light: String


    // Load each Donut from layout, along with respective functions
    private val donutLight by lazy {findViewById<DonutProgressView>(R.id.light_donut)}
    private val donutWater by lazy {findViewById<DonutProgressView>(R.id.water_donut)}
    private val donutHumidity by lazy {findViewById<DonutProgressView>(R.id.humidity_donut)}
    private val donutTemperature by lazy {findViewById<DonutProgressView>(R.id.temperature_donut)}

    // Values for each Donut
    private var lightColor: Int = 0
    private var lightProgress: Float = 0.0F
    private var lightSection: DonutSection = DonutSection("section_light", lightColor, 0F)
    private val lightText by lazy {findViewById<TextView>(R.id.light_percentage)}

    private var waterColor: Int = 0
    private var waterProgress: Float = 0.0F
    private var waterSection: DonutSection = DonutSection("section_water", waterColor, 0F)
    private val waterText by lazy {findViewById<TextView>(R.id.water_percentage)}

    private var humidityColor: Int = 0
    private var humidityProgress: Float = 0.0F
    private var humiditySection: DonutSection = DonutSection("section_humidity", humidityColor, 0F)
    private val humidityText by lazy {findViewById<TextView>(R.id.humidity_percentage)}

    private var temperatureColor: Int = 0
    private var temperatureProgress: Float = 0.0F
    private var temperatureSection: DonutSection = DonutSection("section_temperature", temperatureColor, 0F)
    private val temperatureText by lazy {findViewById<TextView>(R.id.temperature_percentage)}
    // Ends here

    private lateinit var data: VaseData
    private var batteryPercentage: Float = 100.0F

    // Variable to see if it is the first time loading
    private var firstTimeBoot : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_donuts)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Updating...")
        progressDialog.setCanceledOnTouchOutside(false)
        //init firebase auth
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Auth
        uid = auth.currentUser?.uid.toString()

        val bundle : Bundle?= intent.extras
        val vaseName = bundle!!.getString("vaseName")
        vaseID = bundle.getString("ID").toString()
        id_vase.text = vaseName
        /*graphButton.setOnClickListener {

            val Vaso : String = id_vase.text.toString()
            if(Vaso.isNotEmpty()){
                //readData(Vaso)

            }else{
                Toast.makeText(this, "Please enter the Vase name", Toast.LENGTH_SHORT).show()
            }
        }*/

        // Code goes here
        setupDonut()

        databaseReference = FirebaseDatabase.getInstance().getReference("Measures")
        getVaseData()
        progressDialog.dismiss()
        updateAllDonuts(lightProgress,waterProgress,humidityProgress,temperatureProgress)
        updateBattery(batteryPercentage)
        //time()
        refreshApp()
    }

    /*private fun readData(Vaso: String){

        databaseReference = FirebaseDatabase.getInstance().getReference("Measures")
        databaseReference.child(Vaso).get().addOnSuccessListener {
        if(it.exists()){

            val battery = it.child("Battery").value
            val humidity = it.child("Humidity").value
            val level = it.child("Level").value
            val light = it.child("Light").value
            val temperature = it.child("Temperature").value
            Toast.makeText(this, "Sucessfuly Read", Toast.LENGTH_SHORT).show()
            binding.etVaso.text.clear()
            binding.tvBattery.text = battery.toString()
            binding.tvHumidity.text = humidity.toString()
            binding.tvLevel.text = level.toString()
            binding.tvLight.text = light.toString()
            binding.tvTemperature.text = temperature.toString()

        }else{
            Toast.makeText(this, "Vase doesn´t exist", Toast.LENGTH_SHORT).show()
        }
        }.addOnFailureListener{
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()

        }
    }*/



    private fun time() {
        val handler = Handler()
        val delay = 2000 // 1000 milliseconds == 1 second
        val min = 0
        val max = 100
        val minT = 0
        val maxT = 40

        handler.postDelayed(object : Runnable {
            override fun run() {
                lightProgress = (Random.nextInt(max - min + 1) + min).toFloat()
                waterProgress = (Random.nextInt(max - min + 1) + min).toFloat()
                humidityProgress = (Random.nextInt(max - min + 1) + min).toFloat()
                temperatureProgress = (Random.nextInt(maxT - minT + 1) + min).toFloat()
                batteryPercentage = (Random.nextInt(max - min + 1) + min).toFloat()
                updateAllDonuts(lightProgress, waterProgress, humidityProgress, temperatureProgress)
                updateBattery(batteryPercentage)
                handler.postDelayed(this, delay.toLong())
            }
        }, delay.toLong())
    }

    private fun getVaseData(){
        progressDialog.show()
        databaseReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                data = snapshot.child(vaseID).getValue(VaseData::class.java)!!
                lightProgress = data.light!!
                waterProgress = data.water!!
                temperatureProgress = data.temperature!!
                humidityProgress = data.humidity!!
                batteryPercentage = data.battery!!

                if(firstTimeBoot == 0){
                    setupLightDonut(lightProgress)
                    setupHumidityDonut(humidityProgress)
                    setupTemperatureDonut(temperatureProgress)
                    setupWaterDonut(waterProgress)
                    runInitialAnimation()

                    updateBattery(batteryPercentage)
                    firstTimeBoot = 1
                }
                else if(firstTimeBoot == 1){
                    setupLightDonut(lightProgress)
                    setupHumidityDonut(humidityProgress)
                    setupTemperatureDonut(temperatureProgress)
                    setupWaterDonut(waterProgress)
                    updateBattery(batteryPercentage)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read values.", error.toException())
            }

        })
        progressDialog.dismiss()
    }

    private fun setupDonut() {
        //Setup Light Donut
        donutLight.cap = 5000F

        //Setup Water Donut
        donutWater.cap = 100F

        //Setup Humidity Donut
        donutHumidity.cap = 100F

        //Setup Temperature Donut
        donutTemperature.cap = 100F
    }
    private fun setupLightDonut(progress :Float){
        lightColor = getColorLight(progress)
        lightSection = DonutSection("section_light", lightColor, 5000F)
        donutLight.submitData(listOf(lightSection))
        @SuppressLint("SetTextI18n")
        lightText.text = "$progress Lm"
    }
    private fun setupWaterDonut(progress :Float){
        waterColor = getColor(progress)
        waterSection = DonutSection("section_water", waterColor, progress)
        donutWater.submitData(listOf(waterSection))
        @SuppressLint("SetTextI18n")
        waterText.text = "$progress%"
    }
    private fun setupHumidityDonut(progress :Float){
        humidityColor = getColor(progress)
        humiditySection = DonutSection("section_humidity", humidityColor, progress)
        donutHumidity.submitData(listOf(humiditySection))
        @SuppressLint("SetTextI18n")
        humidityText.text = "$progress%"
    }
    private fun setupTemperatureDonut(progress :Float){
        temperatureColor = getColorTemp(progress)
        temperatureSection = DonutSection("section_temperature", temperatureColor, 100F)
        donutTemperature.submitData(listOf(temperatureSection))
        @SuppressLint("SetTextI18n")
        temperatureText.text = "$progress ºC"
    }
    private fun runInitialAnimation() {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1500
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                donutLight.masterProgress = it.animatedValue as Float
                donutLight.alpha = it.animatedValue as Float

                donutWater.masterProgress = it.animatedValue as Float
                donutWater.alpha = it.animatedValue as Float

                donutHumidity.masterProgress = it.animatedValue as Float
                donutHumidity.alpha = it.animatedValue as Float

                donutTemperature.masterProgress = it.animatedValue as Float
                donutTemperature.alpha = it.animatedValue as Float
            }
            start()
        }
    }
    private fun getColor(power: Float): Int {
        val h = (power/100F) * 110F  // Hue (In Degrees! 110º = Max number possible, which means Green)
        val s = 0.9F                 // Saturation
        val b = 0.8F                 // Brightness
        return Color.HSVToColor(floatArrayOf(h, s, b))
    }

    private fun getColorLight(power: Float): Int {
        var h = (power/1750F) * 110F  // Hue (In Degrees! 110º = Max number possible, which means Green)
        if(power >= 1750F) {
            h = 110F
        }
        val s = 0.9F                 // Saturation
        val b = 0.8F                 // Brightness
        return Color.HSVToColor(floatArrayOf(h, s, b))
    }

    private fun getColorTemp(power: Float): Int {
        var h = 0F
        if(power <= 20F) {
            h = (power / 20F) * 110F  // Hue (In Degrees! 110º = Max number possible, which means Green)
        }
        else if(power > 20F) {
            h = (1-(((power/20F)-1))) * 110
        }
        val s = 0.9F                        // Saturation
        val b = 0.8F                        // Brightness
        return Color.HSVToColor(floatArrayOf(h, s, b))
    }
    private fun updateAllDonuts(light:Float, water:Float, humidity:Float, temperature:Float) {
        setupLightDonut(light)
        setupWaterDonut(water)
        setupHumidityDonut(humidity)
        setupTemperatureDonut(temperature)
    }
    private fun updateBattery(battery:Float) {
        when {
            battery <= 10F -> {
                battery1.visibility = View.GONE
                battery2.visibility = View.GONE
                battery3.visibility = View.GONE
                battery4.visibility = View.GONE
                batteryWarning.visibility = View.VISIBLE
            }
            battery <= 25F -> {
                battery1.visibility = View.VISIBLE
                battery2.visibility = View.GONE
                battery3.visibility = View.GONE
                battery4.visibility = View.GONE
                battery1.setColorFilter(resources.getColor(R.color.battery1), android.graphics.PorterDuff.Mode.SRC_IN)
                batteryWarning.visibility = View.GONE
            }
            battery in 25F .. 50F -> {
                battery1.visibility = View.VISIBLE
                battery2.visibility = View.VISIBLE
                battery3.visibility = View.GONE
                battery4.visibility = View.GONE
                battery1.setColorFilter(resources.getColor(R.color.battery2), android.graphics.PorterDuff.Mode.SRC_IN)
                battery2.setColorFilter(resources.getColor(R.color.battery2), android.graphics.PorterDuff.Mode.SRC_IN)
                batteryWarning.visibility = View.GONE
            }
            battery in 50F .. 75F -> {
                battery1.visibility = View.VISIBLE
                battery2.visibility = View.VISIBLE
                battery3.visibility = View.VISIBLE
                battery4.visibility = View.GONE
                battery1.setColorFilter(resources.getColor(R.color.battery3), android.graphics.PorterDuff.Mode.SRC_IN)
                battery2.setColorFilter(resources.getColor(R.color.battery3), android.graphics.PorterDuff.Mode.SRC_IN)
                battery3.setColorFilter(resources.getColor(R.color.battery3), android.graphics.PorterDuff.Mode.SRC_IN)
                batteryWarning.visibility = View.GONE
            }
            battery in 75F .. 100F -> {
                battery1.visibility = View.VISIBLE
                battery2.visibility = View.VISIBLE
                battery3.visibility = View.VISIBLE
                battery4.visibility = View.VISIBLE
                battery1.setColorFilter(resources.getColor(R.color.battery4), android.graphics.PorterDuff.Mode.SRC_IN)
                battery2.setColorFilter(resources.getColor(R.color.battery4), android.graphics.PorterDuff.Mode.SRC_IN)
                battery3.setColorFilter(resources.getColor(R.color.battery4), android.graphics.PorterDuff.Mode.SRC_IN)
                battery4.setColorFilter(resources.getColor(R.color.battery4), android.graphics.PorterDuff.Mode.SRC_IN)
                batteryWarning.visibility = View.GONE
            }
        }
    }
    private fun refreshApp() {
        refresh.setOnRefreshListener {
            /*
            // Start
            batteryPercentage = 9F
            updateBattery(batteryPercentage)
            // Setup Light Donut
            lightProgress = 10F
            setupLightDonut(lightProgress)
            // Setup Water Donut
            waterProgress = 10F
            setupWaterDonut(waterProgress)
            // Setup Humidity Donut
            humidityProgress = 10F
            setupHumidityDonut(humidityProgress)
            // Setup Temperature Donut
            temperatureProgress = 10F
            setupTemperatureDonut(temperatureProgress)
            // End
            */
            getVaseData()
            setupLightDonut(lightProgress)
            setupWaterDonut(waterProgress)
            setupHumidityDonut(humidityProgress)
            setupTemperatureDonut(temperatureProgress)
            updateBattery(batteryPercentage)
            runInitialAnimation()

            Toast.makeText(this, "Page Refreshed!", Toast.LENGTH_SHORT).show()
            refresh.isRefreshing = false

        }
    }
}





