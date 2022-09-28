package com.example.ljobs.Job

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.example.ljobs.User.UserEntity
import java.sql.Blob

class JobRepository (private val jobDao: JobDao){
     val readAllData: LiveData<List<JobEntity>> = jobDao.readAllData()

     suspend fun addJob(job: JobEntity){
          jobDao.insert(job)
     }

     fun updateJobAd(title: String,eduRequirement: String,salary: String,desc: String,location: String,type: String,companyInfo: String,jobStatus: String, id: Int){
          jobDao.update(title,eduRequirement,salary,desc,location,type,companyInfo,jobStatus,id)
     }

     fun fetchJobById(id: Int) : JobEntity{
         return jobDao.fetchJobById(id)
     }

     fun updateJobStatus(jobStatus: String, id: Int){
          jobDao.updateStatus(jobStatus,id)
     }

     fun deleteJobAd(id: Int){
          jobDao.deleteJobById(id)
     }

     fun fetchAllByStatus(jobStatus: String) : LiveData<List<JobEntity>>{
          return jobDao.fetchAllByStatus(jobStatus)
     }

}