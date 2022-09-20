package com.example.ljobs.Job

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Insert
    fun insert(JobEntity: JobEntity)

    @Update
    fun update(JobEntity: JobEntity)

    @Delete
    fun delete(JobEntity: JobEntity)

    @Query("SELECT * FROM `job-table`")
    fun fetchAllUsers(): Flow<List<JobEntity>>

    @Query("SELECT * FROM `job-table` where id=:id")
    fun fetchEduById(id:Int): Flow<JobEntity>
}