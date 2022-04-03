package com.ua.oneleaf

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection

class DataActivity : AppCompatActivity() {

    private val donutLight by lazy {findViewById<DonutProgressView>(R.id.light_donut)}
    private val donutWater by lazy {findViewById<DonutProgressView>(R.id.water_donut)}
    private val donutHumidity by lazy {findViewById<DonutProgressView>(R.id.humidity_donut)}
    private val donutTemperature by lazy {findViewById<DonutProgressView>(R.id.temperature_donut)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_data_fragment)

        // Code goes here
        setupDonut()

        var masterprogress= 40F
        var amount1 = 0F
        var amount2 = 0F
        var amount3 = 0F
        when {
            masterprogress <= 33F -> {
                amount1 = masterprogress
                amount2 = 0F
                amount3 = 0F
            }
            masterprogress in 33F..66F -> {
                amount1 = 33F
                amount2 = masterprogress - 33F
                amount3 = 0F
            }
            masterprogress in 66F..100F -> {
                amount1 = 33F
                amount2 = 33F
                amount3 = 66F - masterprogress
            }
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
        donutLight.submitData(listOf(section1, section2, section3))


    }


    private fun setupDonut() {
        //Setup Light Donut
        donutLight.cap = 100f
        donutLight.masterProgress = 0f
        donutLight.gapAngleDegrees = 0f

        //Setup Water Donut
        donutWater.cap = 100f
        donutWater.masterProgress = 0f
        donutWater.gapAngleDegrees = 0f

        //Setup Humidity Donut
        donutHumidity.cap = 100f
        donutHumidity.masterProgress = 0f
        donutHumidity.gapAngleDegrees = 0f

        //Setup Temperature Donut
        donutTemperature.cap = 100f
        donutTemperature.masterProgress = 0f
        donutTemperature.gapAngleDegrees = 0f
    }
}