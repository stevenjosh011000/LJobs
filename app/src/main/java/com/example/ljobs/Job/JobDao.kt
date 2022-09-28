package com.example.ljobs.Job

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ljobs.User.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(JobEntity: JobEntity)

    @Update
    fun update(JobEntity: JobEntity)

    @Delete
    fun delete(JobEntity: JobEntity)

    @Query("SELECT * FROM `job-table`")
    fun fetchAllUsers(): Flow<List<JobEntity>>

    @Query("SELECT * FROM `job-table` where id=:id")
    fun fetchJobById(id:Int): JobEntity

    @Query("SELECT * FROM `job-table`")
    fun readAllData(): LiveData<List<JobEntity>>

    @Query("SELECT * FROM `job-table` WHERE jobStatus = :jobStatus")
    fun fetchAllByStatus(jobStatus: String?): LiveData<List<JobEntity>>

    @Query("UPDATE `job-table` SET title = :title,eduRequirement = :eduRequirement,salary = :salary, `desc`= :desc, location = :location,type = :type,companyInfo = :companyInfo,jobStatus = :jobStatus WHERE id = :id")
    fun update(title: String?,eduRequirement: String?,salary: String?,desc: String?,location: String?,type: String?,companyInfo: String?,jobStatus: String?, id: Int)

    @Query("UPDATE `job-table` SET jobStatus = :jobStatus WHERE id = :id")
    fun updateStatus(jobStatus: String?, id: Int)

    @Query("DELETE FROM `job-table` WHERE id = :id")
    fun deleteJobById(id: Int)


}