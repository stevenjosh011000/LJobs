package com.example.ljobs.Job

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.User.UserDatabase
import com.example.ljobs.User.UserEntity
import kotlinx.coroutines.*
import java.sql.Blob

class JobViewModel (application: Application): AndroidViewModel(application) {


    val readAllData: LiveData<List<JobEntity>>

    private val repository: JobRepository

    init {
        val jobDao = UserDatabase.getDatabase(application).jobDao()
        repository = JobRepository(jobDao)
        readAllData = repository.readAllData
    }

    fun addJob(job: JobEntity){
        viewModelScope.launch(Dispatchers.IO){
            repository.addJob(job)
        }
    }

    fun updateJobAd(title: String,eduRequirement: String,salary: String,desc: String,location: String,type: String,companyInfo: String,jobStatus: String, id: Int){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateJobAd(title,eduRequirement,salary,desc,location,type,companyInfo,jobStatus, id)
        }
    }

    fun updateJobStatus(jobStatus: String, id: Int){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateJobStatus(jobStatus, id)
        }
    }

    fun deleteJobAd(id: Int){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteJobAd(id)
        }
    }

    fun fetchJobById(id: Int) : JobEntity{
        return runBlocking(viewModelScope.coroutineContext) {
            return@runBlocking repository.fetchJobById(id)
        }
    }

    fun fetchAllByStatus(jobStatus: String) : LiveData<List<JobEntity>>{
        return runBlocking(viewModelScope.coroutineContext) {
            return@runBlocking repository.fetchAllByStatus(jobStatus)
        }
    }



}