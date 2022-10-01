package com.example.ljobs.JobApplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ljobs.Job.JobEntity
import com.example.ljobs.JobApplication.JobApplicationEntity
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.User.UserDatabase
import kotlinx.coroutines.*

class JobApplicationViewModel (application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<JobApplicationEntity>>

    private val repository: JobApplicationRepository

    init {
        val jobApplicationDao = UserDatabase.getDatabase(application).jobApplicationDao()
        repository = JobApplicationRepository(jobApplicationDao)
        readAllData = repository.readAllData
    }

    fun insertApplication(application: JobApplicationEntity){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertApplication(application)
        }
    }

    fun fetchApplicationByEmail(email:String) : Array<Int> {
            return repository.fetchApplicationByEmail(email)
    }
    fun fetchByJobId(jobId:Int): LiveData<List<JobApplicationEntity>> {
        return repository.fetchByJobId(jobId)
    }

    fun deleteApplication(id:Int){
        repository.deleteApplication(id)
    }

    fun deleteApplicationWithJobIdAndEmail(id:Int, email:String){
        repository.deleteApplicationWithJobIdAndEmail(id,email)
    }

    fun fetchByJobIdAndEmail(jobId: Int,email: String): JobApplicationEntity {
        return repository.fetchByJobIdAndEmail(jobId,email)
    }
}
