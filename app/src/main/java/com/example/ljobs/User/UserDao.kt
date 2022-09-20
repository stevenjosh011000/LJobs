package com.example.ljobs.User


import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM `user-table`")
    fun fetchAllUsers(): LiveData<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM `user-table` WHERE email = :email)")
    fun isEmailExist(email: String ) : Boolean

    @Query("UPDATE `user-table` SET resume = :resume,resumeName = :resumeName,resumeStatus = :resumeStatus WHERE email = :email")
    fun update(resume: String?,resumeName:String?,resumeStatus:String?, email: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(userEntity: UserEntity)

    @Update
    suspend fun update(userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)

    @Query("SELECT * FROM `user-table`")
    fun readAllData(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM `user-table` where email=:email")
    fun fetchByEmail(email:String): UserEntity


}
