package com.example.ljobs.JobApplication

import androidx.lifecycle.LiveData
import com.example.ljobs.Job.JobEntity
import com.example.ljobs.JobApplication.JobApplicationDao
import com.example.ljobs.JobApplication.JobApplicationEntity
import kotlinx.coroutines.flow.Flow

class JobApplicationRepository (private val JobApplicationDao: JobApplicationDao) {
    val readAllData: LiveData<List<JobApplicationEntity>> = JobApplicationDao.readAllData()

    fun insertApplication(application: JobApplicationEntity) {
        JobApplicationDao.insertApplication(application)
    }

    fun fetchApplicationByEmail(email:String) : Array<Int> {
        return JobApplicationDao.fetchApplicationByEmail(email)
    }

    fun fetchApplicationById(id:Int) : Flow<JobApplicationEntity> {
        return JobApplicationDao.fetchApplicationById(id)
    }

    fun fetchAllApplication(): Flow<List<JobApplicationEntity>> {
        return JobApplicationDao.fetchAllApplication()
    }

    fun deleteApplication(id:Int){
        return JobApplicationDao.deleteApplication(id)
    }

    fun deleteApplicationWithJobIdAndEmail(id:Int, email:String){
        return JobApplicationDao.deleteApplicationWithJobIdAndEmail(id,email)
    }

    fun fetchByJobId(jobId: Int): LiveData<List<JobApplicationEntity>> {
        return JobApplicationDao.fetchByJobId(jobId)
    }

    fun fetchByJobIdAndEmail(jobId: Int,email: String): JobApplicationEntity {
        return JobApplicationDao.fetchByJobIdAndEmail(jobId,email)
    }

}
