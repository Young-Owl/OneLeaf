//Objeto criado por (H) a seguir o toturial... How to Retrieve Data from Firebase database (Foxandroid)




package com.ua.oneleaf

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingComponent
import com.google.firebase.database.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ReadData  :   AppCompatActivity(){
    private lateinit var binding : ActivityReadDataBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityReadDataBinding.inflate(LayoutInflater)
        setContentView(binding.root)

        binding.readdatBtn.setOnClickListener {             //função para clicar no botão para ir buscar a info

            val Vaso : String = binding.etVaso.text.toString()
            if(Vaso.isNotEmpty()){
                readData(Vaso)

            }else{
                Toast.makeText(this, "Please enter the Vase name", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun readData(Vaso: String){

        database = FirebaseDatabase.getInstance().getReference("Measures")
        database.child(Vaso).get().addOnSuccessListener {
            if(it.exists()){

                val battery = it.child("Battery").value
                val humidity = it.child("Humidity").value
                val level = it.child("level").value
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
    }


}
