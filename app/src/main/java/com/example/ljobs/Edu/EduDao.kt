package com.example.ljobs.Edu

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EduDao {

    @Insert
    fun insert(eduEntity: EduEntity)

    @Update
    fun update(eduEntity: EduEntity)

    @Delete
    fun delete(eduEntity: EduEntity)

    @Query("SELECT * FROM `edu-table` where email=:email")
    fun fetchAllEduByEmail(email:String): Flow<List<EduEntity>>

    @Query("SELECT * FROM `edu-table` where id=:id")
    fun fetchEduById(id:Int): Flow<EduEntity>
}