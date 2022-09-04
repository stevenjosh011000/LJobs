package com.example.ljobs.Session

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.ljobs.HomeActivity
import com.example.ljobs.IntroActivity

class LoginPref {

    lateinit var pref:SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    lateinit var con: Context
    var PRIVATEMODE : Int = 0

    constructor(con:Context){
        this.con = con
        pref = con.getSharedPreferences(PREF_NAME,PRIVATEMODE)
        editor = pref.edit()
    }

    companion object{
        val PREF_NAME = "Login_Preference"
        val IS_LOGIN = "isLoggedin"
        val KEY_EMAIL = "email"
    }

    fun createLoginSession(email:String){
        editor.putBoolean(IS_LOGIN,true)
        editor.putString(KEY_EMAIL,email)
        editor.commit()
    }

    fun checkLogin(){
        if(!this.isLoggedin()){
            val i : Intent = Intent(con,HomeActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            con.startActivity(i)
        }
    }

    fun isLoggedin(): Boolean {
        return pref.getBoolean(IS_LOGIN,false)
    }

    fun getUserDetails():HashMap<String,String>{
        val user : Map<String,String> = HashMap<String,String>()
        (user as HashMap).put(KEY_EMAIL, pref.getString(KEY_EMAIL,null)!!)
        return user
    }

    fun logoutUser(){
        editor.clear()
        editor.commit()
        val i : Intent = Intent(con,IntroActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        con.startActivity(i)
    }




}