package com.example.ljobs.User

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ljobs.Session.LoginPref
import kotlinx.coroutines.*

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

    fun updateUser(user: UserEntity){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateUser(user)
        }
    }

}