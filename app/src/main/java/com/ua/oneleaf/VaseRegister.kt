package com.ua.oneleaf

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.vase_register.*

class VaseRegister : AppCompatActivity() {


    private val ref = FirebaseAuth.getInstance()
    private var filePath: Uri? = null
    private lateinit var vase: Vase
    private lateinit var usern: String
    private lateinit var emailu: String
    private lateinit var vaseNumber: String
    private lateinit var auth: FirebaseAuth;
    private lateinit var storageReference: StorageReference
    private lateinit var binding : AccountHomeActivity
    private lateinit var user: com.ua.oneleaf.User
    private lateinit var databaseReference: DatabaseReference
    private lateinit var uid: String


    private var vaseRVal = ""
    private var plantRVal = ""
    private var humidityRVal = ""

    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vase_register)

        // configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Registering Vase...")
        progressDialog.setCanceledOnTouchOutside(false)
        //init firebase auth
        auth = FirebaseAuth.getInstance()

        uid = auth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        getVaseData()
        vaseRegistBtn.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        vaseRVal = vase_name.text.toString().trim()
        plantRVal = id_vase.text.toString().trim()
        //validate data
        if (TextUtils.isEmpty(vaseRVal)){
            //Invalid email format
            vase_name.error = "Please enter the vase's name."
        }
        else if (TextUtils.isEmpty(plantRVal)){
            //Password isn't entered
            id_vase.error = "Please enter the vase's ID."
        }
        else{
            // Data is valid, continue SignUp
            registerVase()
        }
    }

    private fun registerVase() {
        //show progress
        progressDialog.show()
        //create account

        vaseRVal = vase_name.text.toString().trim()
        plantRVal = id_vase.text.toString().trim()
        humidityRVal = humidity_default.text.toString().trim()



        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        if (humidityRVal == ""){
            humidityRVal = "70"
        }
        vaseNumber = (vaseNumber.toInt() + 1).toString()
        vase = Vase(vaseRVal,plantRVal,humidityRVal)

        user = User(usern,emailu,vaseNumber)
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/$uid/Data")
        databaseReference.setValue(user).addOnCompleteListener{
            if(it.isSuccessful){
            }
            else{
                Toast.makeText(this, "Failed to register.", Toast.LENGTH_SHORT).show()
            }
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Users/$uid/Vases")
        databaseReference.child(vaseNumber).setValue(vase).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(this, "Vase Registered!", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                startActivity(Intent(this, AccountHomeActivity::class.java))
            }
            else{
                Toast.makeText(this, "Failed to register.", Toast.LENGTH_SHORT).show()
            }
        }



        progressDialog.dismiss()
    }

    private fun getVaseData(){
        databaseReference.child("$uid/Data").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user = snapshot.getValue(com.ua.oneleaf.User::class.java)!!
                usern = user.username.toString()
                emailu = user.email.toString()
                vaseNumber = user.vases.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                vaseNumber = "0"
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }


        })
    }
}
