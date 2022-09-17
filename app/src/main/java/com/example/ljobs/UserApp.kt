package com.example.ljobs

import android.app.Application
import com.example.ljobs.Edu.EduDatabase
import com.example.ljobs.Edu.LanguageDatabase
import com.example.ljobs.User.UserDatabase

class UserApp : Application() {

    val db by lazy{
        UserDatabase.getDatabase(this)
    }

    val dbEdu by lazy {
        EduDatabase.getDatabase(this)
    }

    val dbLanguage by lazy {
        LanguageDatabase.getDatabase(this)
    }

}