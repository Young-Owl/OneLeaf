package com.ua.oneleaf

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.update_profile.*
import java.io.IOException

class ProfileUpdate : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private lateinit var storageReference: StorageReference
    private val ref = FirebaseAuth.getInstance()
    private var filePath: Uri? = null
    private lateinit var uid: String

    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    companion object{
        const val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_profile)

        // configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Updating...")
        progressDialog.setCanceledOnTouchOutside(false)
        //init firebase auth
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Auth
        auth = Firebase.auth
        uid = auth.currentUser?.uid.toString()


        profileUp.setOnClickListener{
            pickImageGallery()
        }

        updateBtn.setOnClickListener {
            uploadProfilePic()
        }

    }

    private fun uploadProfilePic() {
        progressDialog.show()
        if(filePath != null){
            storageReference = FirebaseStorage.getInstance().getReference("Users/"+auth.currentUser?.uid+".png")
            storageReference.putFile(filePath!!).addOnSuccessListener {
                Toast.makeText(this@ProfileUpdate, "Profile Pic Updated", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                startActivity(Intent(this, AccountHomeActivity::class.java))
            }.addOnFailureListener{
                Toast.makeText(this@ProfileUpdate, "Profile Pic Failed", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                profileUp.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}