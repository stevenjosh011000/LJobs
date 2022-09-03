package com.example.ljobs

import android.app.Application

class UserApp : Application() {

    var USER_EMAIL : String? = null

    val db by lazy{
        UserDatabase.getDatabase(this)
    }

}