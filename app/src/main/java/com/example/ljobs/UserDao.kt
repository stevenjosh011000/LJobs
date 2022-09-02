package com.example.ljobs


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    fun insert(userEntity: UserEntity)

    @Update
    fun update(employeeEntity: UserEntity)

    @Delete
    fun delete(employeeEntity: UserEntity)

    @Query("SELECT * FROM `user-table`")
    fun fetchAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM `user-table` where id=:id")
    fun fetchUserById(id:Int): Flow<UserEntity>
}
