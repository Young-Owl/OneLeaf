package com.ua.oneleaf

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract
import android.service.autofill.UserData
import java.security.AccessControlContext

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
}