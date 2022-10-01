package com.example.ljobs.Job

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ljobs.User.UserDatabase
import com.example.ljobs.User.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

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


    fun fetchByEmail(email:String) : LiveData<List<JobEntity>> {

        return runBlocking(viewModelScope.coroutineContext) {
            return@runBlocking repository.fetchByEmail(email)
        }
    }

    fun filterPendingJob() : LiveData<List<JobEntity>> {

        return runBlocking(viewModelScope.coroutineContext) {
            return@runBlocking repository.filterPendingJob()
        }
    }


    fun fetchJobById(id: Int): List<JobEntity> {
        return repository.fetchJobById(id)
    }


    fun updateJobStatus(jobStatus: String, id: Int){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateJobStatus(jobStatus, id)
        }
    }


    fun fetchAllJob(): Flow<List<JobEntity>> {
        return runBlocking(viewModelScope.coroutineContext) {
            return@runBlocking repository.fetchAllJob()
        }
    }

    fun fetchByApplicationEmail(email: String): List<JobEntity> {
        return repository.fetchByApplicationEmail(email)
    }


    fun updateJob(
        title: String,
        eduRequirement: String, salary: String, descrip: String, location: String, type: String, companyInfo: String, id: Int
    ){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateJob(title,eduRequirement,salary,descrip, location,type, companyInfo,id)
        }
    }

    fun deleteJob(id:Int){
        viewModelScope.launch ( Dispatchers.IO ){
            repository.deleteJob(id)
        }
    }
    fun emailExist(email:String) : Boolean {

        return runBlocking(viewModelScope.coroutineContext) {
            return@runBlocking repository.emailExist(email)
        }
    }

    fun fetchByFilteringCurrentUser(email: String): LiveData<List<JobEntity>> {
        return repository.fetchByFilteringCurrentUser(email)
    }


    fun updateJobAd(title: String,eduRequirement: String,salary: String,desc: String,location: String,type: String,companyInfo: String,jobStatus: String, id: Int){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateJobAd(title,eduRequirement,salary,desc,location,type,companyInfo,jobStatus, id)
        }
    }

    fun fetchAllByStatus(jobStatus: String) : LiveData<List<JobEntity>>{
        return runBlocking(viewModelScope.coroutineContext) {
            return@runBlocking repository.fetchAllByStatus(jobStatus)
        }
    }

    fun deleteJobAd(id: Int){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteJobAd(id)
        }
    }

    fun fetchJobEntityById(id: Int) : JobEntity{
        return runBlocking(viewModelScope.coroutineContext) {
            return@runBlocking repository.fetchJobEntityById(id)
        }
    }
}