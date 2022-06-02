package com.ua.oneleaf


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class FirebaseHelper : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val ref = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val email = findViewById<EditText>(R.id.email_register)
        val password = findViewById<EditText>(R.id.password_register)
        val registerBtn = findViewById<Button>(R.id.save)

        registerBtn.setOnClickListener{
            ref.createUserWithEmailAndPassword(
                email.text.toString().trim(),
                password.text.toString().trim()
            )
        }

    }

}

/*// Read from the database(deve faltar aqui uma ver) voltar a isto !! ((H))
myRef.addValueEventListener(object : Sampler.Value { override fun onDataChange(dataSnapshot: DataSnapshot){
    //This metod is called once with the intial value and again
    //whenever data at this location is updated
    val value = dataSnapshot.getValue<String>()
    Log.d(TAG, "Value is: $value")
}
    override fun onCancelled(error: DatabaseError){
        //failed to read value
        Log.w(TAG, "Failed to read value", error.toException())
    }
})
*/




