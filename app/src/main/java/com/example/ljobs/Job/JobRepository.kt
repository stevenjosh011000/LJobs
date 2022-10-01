package com.example.ljobs.Job

import androidx.lifecycle.LiveData
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

class JobRepository (private val JobDao: JobDao){
     val readAllData: LiveData<List<JobEntity>> = JobDao.readAllData()

     suspend fun addJob(Job:JobEntity){
          JobDao.insert(Job)
     }

     fun updateJob(
          title: String,
          eduRequirement:String, salary:String, descrip:String, location:String, type:String, companyInfo:String, id:Int){
          JobDao.update(title,eduRequirement, salary, descrip, location, type, companyInfo, id)
     }

     fun fetchByEmail(email:String) : LiveData<List<JobEntity>> {
          return JobDao.fetchByEmail(email)
     }
     fun updateJobStatus(jobStatus: String, id:Int){
          return JobDao.updateJobStatus(jobStatus,id)
     }
     fun fetchJobById(id:Int) : List<JobEntity> {
          return JobDao.fetchJobById(id)
     }
     fun fetchAllJob(): Flow<List<JobEntity>> {
          return JobDao.fetchAllJobs()
     }

     fun deleteJob(id:Int){
          return JobDao.deleteJob(id)
     }

     fun filterPendingJob(): LiveData<List<JobEntity>> {
          return JobDao.filterPendingJob()
     }

     fun fetchByApplicationEmail(email: String): List<JobEntity> {
          return JobDao.fetchByApplicationEmail(email)
     }
     fun emailExist(email:String) : Boolean {
          return JobDao.isEmailExist(email)
     }

     fun fetchByFilteringCurrentUser(email: String): LiveData<List<JobEntity>> {
          return JobDao.fetchByFilteringCurrentUser(email)
     }

}