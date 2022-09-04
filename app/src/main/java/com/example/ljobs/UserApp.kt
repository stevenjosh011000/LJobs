package com.example.ljobs

import android.app.Application

class UserApp : Application() {

    val db by lazy{
        UserDatabase.getDatabase(this)
    }

}