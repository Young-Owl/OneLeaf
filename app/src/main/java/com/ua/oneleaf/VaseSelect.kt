package com.ua.oneleaf

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class VaseSelect : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private lateinit var databaseReference: DatabaseReference
    private val ref = FirebaseAuth.getInstance()
    private var filePath: Uri? = null
    private lateinit var uid: String
    private lateinit var progressDialog: ProgressDialog
    private lateinit var vaseRecyclerList: RecyclerView
    private lateinit var vaseArrayList: ArrayList<Vase>

    var vases = ArrayList<Vase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vase_select)

        // configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Getting List...")
        progressDialog.setCanceledOnTouchOutside(false)
        //init firebase auth
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Auth
        auth = Firebase.auth
        uid = auth.currentUser?.uid.toString()

        vaseRecyclerList = findViewById(R.id.vaseList)
        vaseRecyclerList.layoutManager = LinearLayoutManager(this)
        vaseRecyclerList.setHasFixedSize(true)

        vaseArrayList = arrayListOf<Vase>()

        getVaseData()

    }

    private fun getVaseData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/$uid/Vases")
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val vase = userSnapshot.getValue(Vase::class.java)
                        vaseArrayList.add(vase!!)
                    }
                    vaseRecyclerList.adapter = RecyclerAdapter(vaseArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        var vases = RecyclerAdapter(vaseArrayList)
        vaseRecyclerList.adapter = vases
        vases.setOnItemClickListener(object : RecyclerAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@VaseSelect, DataActivity::class.java)
                intent.putExtra("vaseName",vaseArrayList[position].vasename)
                intent.putExtra("ID",vaseArrayList[position].vaseID)
                startActivity(intent)
            }

        })


    }


}