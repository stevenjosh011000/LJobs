package com.example.ljobs.JobApplication

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ljobs.Job.JobEntity
import com.example.ljobs.JobApplication.JobApplicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JobApplicationDao {
    @Insert
    fun insertApplication(JobApplicationEntity: JobApplicationEntity)

    @Delete
    fun delete(JobApplicationEntity: JobApplicationEntity)

    @Query("DELETE FROM `application-table` WHERE id=:id")
    fun deleteApplication(id:Int)

    @Query("DELETE FROM `application-table` WHERE jobId=:id AND email=:email")
    fun deleteApplicationWithJobIdAndEmail(id:Int, email:String)

    @Query("SELECT * FROM `application-table`")
    fun fetchAllApplication(): Flow<List<JobApplicationEntity>>

    @Query("SELECT * FROM `application-table` where id=:id")
    fun fetchApplicationById(id:Int): Flow<JobApplicationEntity>

    @Query("SELECT jobId FROM `application-table` where email=:email")
    fun fetchApplicationByEmail(email:String): Array<Int>

//    @Query("SELECT * FROM `application-table` where email=:email")
//    fun fetchApplicationByEmail(email:String): List<JobApplicationEntity>

    @Query("SELECT * FROM `application-table`")
    fun readAllData(): LiveData<List<JobApplicationEntity>>

    @Query("SELECT * FROM `application-table` where jobId=:jobId")
    fun fetchByJobId(jobId:Int): LiveData<List<JobApplicationEntity>>

    @Query("SELECT * FROM `application-table` where jobId=:jobId AND email=:email")
    fun fetchByJobIdAndEmail(jobId:Int, email:String): JobApplicationEntity

}