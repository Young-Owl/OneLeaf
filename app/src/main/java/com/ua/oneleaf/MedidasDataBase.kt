//Objeto criado por (H) a seguir o toturial...

package com.ua.oneleaf

import android.util.Log
import com.google.firebase.database.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

object MedidasDataBase: Observable() {
    private var mValueDataListener: ValueEventListener? = null
    private var mMesaureList: ArrayList<DataBaseHelper.Medidas>? = ArrayList()

    private fun getDataBaseRef() : DatabaseReference?{
        return FirebaseDatabase.getInstance().reference.child("Measures")
    }

    init {
        if(mValueDataListener != null) {
            getDataBaseRef()?.removeEventListener(mValueDataListener)

        }
        mValueDataListener = null
        Log.i("MedidasDataBase", "data init line 24")

        mValueDataListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    Log.i("MedidasDataBase", "data updated line 28")
                }catch(e: Exception){
                    e.printStackTrace()     //min 18:51
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
    }
}