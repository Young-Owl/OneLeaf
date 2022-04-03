package com.ua.oneleaf

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection

class DataActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_data_fragment)

        // Code goes here
        setupDonut()

        // Setup Light Donut
        lightProgress = 32F
        setupLightDonut(lightProgress)

        // Setup Water Donut
        waterProgress = 66F
        setupWaterDonut(waterProgress)

        // Setup Humidity Donut
        humidityProgress = 80F
        setupHumidityDonut(humidityProgress)

        // Setup Temperature Donut
        temperatureProgress = 40F
        setupTemperatureDonut(temperatureProgress)
        runInitialAnimation()
    }


    private fun setupDonut() {
        //Setup Light Donut
        donutLight.cap = 100F

        //Setup Water Donut
        donutWater.cap = 100F

        //Setup Humidity Donut
        donutHumidity.cap = 100F

        //Setup Temperature Donut
        donutTemperature.cap = 100F
    }
    private fun setupLightDonut(progress :Float){
        lightColor = getColor(progress)
        lightSection = DonutSection("section_light", lightColor, progress)
        donutLight.submitData(listOf(lightSection))
        lightText.text = "$progress%"
    }
    private fun setupWaterDonut(progress :Float){
        waterColor = getColor(progress)
        waterSection = DonutSection("section_water", waterColor, progress)
        donutWater.submitData(listOf(waterSection))
        waterText.text = "$progress%"
    }
    private fun setupHumidityDonut(progress :Float){
        humidityColor = getColor(progress)
        humiditySection = DonutSection("section_humidity", humidityColor, progress)
        donutHumidity.submitData(listOf(humiditySection))
        humidityText.text = "$progress%"
    }
    private fun setupTemperatureDonut(progress :Float){
        temperatureColor = getColorTemp(progress)
        temperatureSection = DonutSection("section_temperature", temperatureColor, 100F)
        donutTemperature.submitData(listOf(temperatureSection))
        temperatureText.text = "$progress ºC"
    }
    private fun runInitialAnimation() {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1500
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                donutLight.masterProgress = it.animatedValue as Float
                donutLight.alpha = it.animatedValue as Float
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
}

