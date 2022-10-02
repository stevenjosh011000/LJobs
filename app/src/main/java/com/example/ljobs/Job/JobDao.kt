package com.example.ljobs.Job

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ljobs.User.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Insert
    fun insert(JobEntity: JobEntity)

    /*@Update
    fun update(JobEntity: JobEntity)
*/

    @Query("UPDATE `job-table` SET title = :title, eduRequirement = :eduRequirement, salary = :salary, `desc` = :desc, location = :location, type=:type, companyInfo= :companyInfo WHERE id = :id")
    fun update(title: String,eduRequirement:String, salary:String, desc:String, location:String, type:String, companyInfo:String, id:Int)

    @Delete
    fun delete(JobEntity: JobEntity)

    @Query("DELETE FROM `job-table` WHERE id= :id")
    fun deleteJob(id:Int)

    @Query("SELECT * FROM `job-table`")
    fun fetchAllJobs(): Flow<List<JobEntity>>

//    @Query("SELECT * FROM `job-table` where id=:id")
//    fun fetchJobById(id:Int): Flow<JobEntity>

    @Query("SELECT id FROM `job-table` where id=:id")
    fun fetchJobById(id:Int): List<JobEntity>


    @Query("SELECT * FROM `job-table`")
    fun readAllData(): LiveData<List<JobEntity>>


    @Query("SELECT * FROM `job-table` where email=:email")
    fun fetchByEmail(email:String): LiveData<List<JobEntity>>

    @Query("UPDATE `job-table` SET jobStatus = :jobStatus WHERE id = :id")
    fun updateJobStatus(jobStatus: String, id: Int)

    @Query("SELECT * FROM `job-table` WHERE jobStatus != '0' ")
    fun filterPendingJob():LiveData<List<JobEntity>>

    @Query("SELECT * FROM `job-table` WHERE id IN (SELECT jobId FROM `application-table` WHERE email = :email)")
    fun fetchByApplicationEmail(email: String): LiveData<List<JobEntity>>

    @Query("SELECT * FROM `job-table` WHERE email!=:email AND jobStatus != '0'")
    fun fetchByFilteringCurrentUser(email: String): LiveData<List<JobEntity>>

    @Query("SELECT EXISTS(SELECT * FROM `job-table` WHERE email = :email)")
    fun isEmailExist(email: String ) : Boolean

    @Update
    fun update(JobEntity: JobEntity)



    @Query("SELECT * FROM `job-table`")
    fun fetchAllUsers(): Flow<List<JobEntity>>



    @Query("SELECT * FROM `job-table` WHERE jobStatus = :jobStatus")
    fun fetchAllByStatus(jobStatus: String?): LiveData<List<JobEntity>>

    @Query("UPDATE `job-table` SET title = :title,eduRequirement = :eduRequirement,salary = :salary, `desc`= :desc, location = :location,type = :type,companyInfo = :companyInfo,jobStatus = :jobStatus WHERE id = :id")
    fun update(title: String?,eduRequirement: String?,salary: String?,desc: String?,location: String?,type: String?,companyInfo: String?,jobStatus: String?, id: Int)

    @Query("UPDATE `job-table` SET jobStatus = :jobStatus WHERE id = :id")
    fun updateStatus(jobStatus: String?, id: Int)

    @Query("DELETE FROM `job-table` WHERE id = :id")
    fun deleteJobById(id: Int)

    @Query("SELECT * FROM `job-table` where id=:id")
    fun fetchJobEntityById(id:Int): JobEntity

    @Query("SELECT * FROM `job-table` WHERE jobStatus != '0' AND title LIKE '%' || :searchText || '%'")
    fun searchData(searchText: String): LiveData<List<JobEntity>>

    @Query("SELECT * FROM `job-table` where email!=:email AND title LIKE '%' || :searchText || '%'")
    fun fetchByFilteringNSearchData(email:String, searchText: String): LiveData<List<JobEntity>>

}