package com.example.ljobs.User

import androidx.lifecycle.LiveData

class UserRespository (private val userDao: UserDao){
     val readAllData: LiveData<List<UserEntity>> = userDao.readAllData()


}