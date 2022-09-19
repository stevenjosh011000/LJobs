package com.example.ljobs

import android.app.Application
import com.example.ljobs.User.UserDatabase

class UserApp : Application() {

    val db by lazy{
        UserDatabase.getDatabase(this)
    }

}