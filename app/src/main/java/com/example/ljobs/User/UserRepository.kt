package com.example.ljobs.User

import androidx.lifecycle.LiveData

class UserRepository (private val userDao: UserDao){
     val readAllData: LiveData<List<UserEntity>> = userDao.readAllData()

     suspend fun addUser(user:UserEntity){
          userDao.addUser(user)
     }

     suspend fun updateUser(user: UserEntity){
          userDao.update(user)
     }

     fun updateUser(resume: String,resumeName:String,resumeStatus:String, email: String){
          userDao.update(resume,resumeName,resumeStatus,email)
     }

     fun fetchByEmail(email:String) : UserEntity{
         return userDao.fetchByEmail(email)
     }

     fun emailExist(email:String) : Boolean {
          return userDao.isEmailExist(email)
     }


}