package com.example.ljobs.User

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import java.sql.Blob

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

     fun fetchById(id:Int) : UserEntity{
          return userDao.fetchById(id)
     }

     fun emailExist(email:String) : Boolean {
          return userDao.isEmailExist(email)
     }

     fun updateUserProfile(image: ByteArray?, email: String, name: String, phoneNum: String,role:String, id: Int){
          userDao.updateUserProfile(image, email, name, phoneNum,role, id)
     }

     fun deleteUser(id: Int){
          userDao.deleteUserById(id)
     }

}