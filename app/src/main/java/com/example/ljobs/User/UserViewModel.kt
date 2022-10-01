package com.example.ljobs.User

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ljobs.Session.LoginPref
import kotlinx.coroutines.*
import java.sql.Blob

class UserViewModel (application: Application): AndroidViewModel(application) {


    val readAllData: LiveData<List<UserEntity>>

    private val repository: UserRepository

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        readAllData = repository.readAllData
    }

    fun addUser(user: UserEntity){
        viewModelScope.launch(Dispatchers.IO){
            repository.addUser(user)
        }
    }

    fun fetchByEmail(email:String) : UserEntity {

        return runBlocking(viewModelScope.coroutineContext) {
            return@runBlocking repository.fetchByEmail(email)
        }
    }

    fun fetchById(id:Int) : UserEntity {
        return runBlocking(viewModelScope.coroutineContext) {
            return@runBlocking repository.fetchById(id)
        }
    }

    fun emailExist(email:String) : Boolean {

        return runBlocking(viewModelScope.coroutineContext) {
            return@runBlocking repository.emailExist(email)
        }
    }

    fun updateUser(resume: String,resumeName:String,resumeStatus:String, email: String){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateUser(resume,resumeName,resumeStatus, email)
        }
    }

    fun updateUserProfile(image: ByteArray?, email: String, name: String, phoneNum: String,role:String, id: Int){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateUserProfile(image, email, name, phoneNum,role, id)
        }
    }

    fun deleteUser(id: Int){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteUser(id)
        }
    }

    fun updateUser(user: UserEntity){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateUser(user)
        }
    }

    fun fetchManager() : UserEntity {
        return runBlocking(viewModelScope.coroutineContext) {
            return@runBlocking repository.fetchManager()
        }
    }

}