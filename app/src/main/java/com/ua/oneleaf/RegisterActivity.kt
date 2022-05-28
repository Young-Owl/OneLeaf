package com.ua.oneleaf

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.user_registration.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri
    private val ref = FirebaseAuth.getInstance()


    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    private var usernameRVal = ""
    private var emailRVal = ""
    private var passwordRVal = ""

    companion object{
        val IMAGE_REQUEST_CODE = 100
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_registration)

        // configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating account In...")
        progressDialog.setCanceledOnTouchOutside(false)
        //init firebase auth
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Auth
        auth = Firebase.auth
        val uid = auth.currentUser?.uid
        val emailR = findViewById<TextInputEditText>(R.id.email_register)
        val passwordR = findViewById<TextInputEditText>(R.id.password_register)
        val registerB = findViewById<Button>(R.id.save)

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        val user = com.ua.oneleaf.User(usernameRVal,emailRVal)
        if (uid != null) {
            databaseReference.child(uid).setValue(user).addOnCompleteListener{
                if(it.isSuccessful){
                    uploadProfilePic()
                }
            }
        }
        profile_image_up.setOnClickListener{
            pickImageGallery()
        }
        registerB.setOnClickListener {
            validateData()
        }

        loginfromregister.setOnClickListener{
            showLogin()
        }

    }

    private fun showLogin() {
        val intent = Intent(
            this, LoginActivity::class.java
        )
        startActivity(intent)
    }

    private fun showHome() {
        val intent = Intent(
            this, MainActivity::class.java
        )
        startActivity(intent)
    }

    private fun validateData(){
        //get data
        usernameRVal = user_register.text.toString().trim()
        emailRVal = email_register.text.toString().trim()
        passwordRVal = password_register.text.toString().trim()
            //validate data
            if (!Patterns.EMAIL_ADDRESS.matcher(emailRVal).matches()){
                //Invalid email format
                email_register.error = "Invalid email format"
            }
            else if (TextUtils.isEmpty(passwordRVal)){
                //Password isn't entered
                password_register.error = "Please enter password"
            }
            else if (passwordRVal.length < 6){
                password_register.error = "Password must be at least 6 character long"
            }
            else if (TextUtils.isEmpty(usernameRVal)){
                //Username isn't entered
                user_register.error = "Please enter a username"
            }
            else{
                // Data is valid, continue SignUp
                firebaseSignUp()
            }
    }

    private fun firebaseSignUp() {
        val uid = auth.currentUser?.uid
        //show progress
        progressDialog.show()
        //create account
        auth.createUserWithEmailAndPassword(emailRVal, passwordRVal)
            .addOnSuccessListener {
                //Signup success
                progressDialog.dismiss()
                //Get current user
                val firebaseUser = auth.currentUser
                val email = firebaseUser!!.email

                Toast.makeText(this, "Account created with email $email", Toast.LENGTH_SHORT).show()
                //open profile
                startActivity(Intent(this, AccountHomeActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                //Signup failed
                progressDialog.dismiss()
                Toast.makeText(this, "SignUp Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadProfilePic() {
        imageUri = Uri.parse("android.resources://$packageName/${R.drawable.ic_baseline_face}")
        storageReference = FirebaseStorage.getInstance().getReference("User/"+auth.currentUser?.uid)
        storageReference.putFile(imageUri).addOnSuccessListener {
            Toast.makeText(this@RegisterActivity, "Profile Pic Updated", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this@RegisterActivity, "Profile Pic Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            profile_image_up.setImageURI(data?.data)
        }
    }

    // Not working
    private fun updateProfile(){
        auth.currentUser?.let { user->
            val username = user_register.text.toString()
            val photoURl = Uri.parse("android.resource://$packageName/${R.drawable.ic_baseline_face}")
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(photoURl)
                .build()

            CoroutineScope(Dispatchers.IO).launch {
                try{
                    // user.updateProfile(profileUpdates).await()
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@RegisterActivity,"Successfully updated Profile",Toast.LENGTH_LONG).show()                   }
                }catch(e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@RegisterActivity,e.message,Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }

}
