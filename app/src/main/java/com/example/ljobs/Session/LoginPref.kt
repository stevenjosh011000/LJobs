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
        val KEY_ID = "id"
        val KEY_PASS = "password"
        val RESUME = "resume"
        val RESUME_NAME = "resumeName"
        val ROLE = "role"
    }

    fun createLoginSession(id:String,email:String,password:String,resume:String,resumeName:String,role:String){
        editor.putBoolean(IS_LOGIN,true)
        editor.putString(KEY_ID,id)
        editor.putString(KEY_EMAIL,email)
        editor.putString(KEY_PASS,password)
        editor.putString(RESUME,resume)
        editor.putString(RESUME_NAME,resumeName)
        editor.putString(ROLE,role)
        editor.commit()
    }

    fun updateResumeSession(resume:String,resumeName:String){
        editor.putString(RESUME,resume)
        editor.putString(RESUME_NAME,resumeName)
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
        (user as HashMap).put(KEY_ID, pref.getString(KEY_ID,null)!!)
        (user as HashMap).put(KEY_PASS, pref.getString(KEY_PASS,null)!!)
        (user as HashMap).put(RESUME, pref.getString(RESUME,null)!!)
        (user as HashMap).put(RESUME_NAME, pref.getString(RESUME_NAME,null)!!)
        (user as HashMap).put(ROLE, pref.getString(ROLE,null)!!)
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