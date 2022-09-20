package com.example.ljobs.User

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class UserViewModel (application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<UserEntity>>
    private val respository: UserRespository

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        respository = UserRespository(userDao)
        readAllData = respository.readAllData
    }

}