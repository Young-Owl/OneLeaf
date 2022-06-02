package com.ua.oneleaf

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract
import android.service.autofill.UserData
import com.google.firebase.database.DataSnapshot
import java.security.AccessControlContext
import java.util.function.DoubleBinaryOperator

class DataBaseHelper(context: Context): SQLiteOpenHelper(context, dbname, factory, version) {
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("create table user(id integer primary key autoincrement," + "name varchar(30), email varchar(100), password varchar(20)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insertUserData(email: String, password: String){
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = ContentValues()
        values.put("email",email)
        values.put("password",password)

        db.insert("user", null,values )
        db.close()
    }


    fun userPresent(email: String, password: String):Boolean{
        val db = writableDatabase
        val query = "select * from user where email = $email and password = $password"
        val cursor = db.rawQuery(query, null)
        if (cursor.count <= 0){
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    companion object{
        internal val dbname = "userDB"
        internal val factory = null
        internal val version = 1
    }

   //******************************************************
    class Medidas (snapshot: DataSnapshot) {
        lateinit var vase: String
        lateinit var numb_vase: String
        lateinit var Battery: String
        lateinit var Humidity: String
        lateinit var Level: String
        lateinit var Light: String
        lateinit var Temperature: String

        init {
            try {
                val data: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
                vase = snapshot.key ?: ""
                numb_vase = data["1"] as String
                Battery = data["Battery"] as String
                Humidity = data["Humidity"] as String
                Level = data["Level"] as String
                Light = data["light"] as String
                Temperature = data["Temperature"] as String
            } catch(e: Exception){
                e.printStackTrace()
            }
        }
   }


}