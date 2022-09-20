package com.example.ljobs.User


import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    fun insert(userEntity: UserEntity)

    @Update
    fun update(userEntity: UserEntity)

    @Delete
    fun delete(userEntity: UserEntity)

    @Query("SELECT * FROM `user-table`")
    fun readAllData(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM `user-table`")
    fun fetchAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM `user-table` where id=:id")
    fun fetchUserById(id:Int): Flow<UserEntity>

    @Query("SELECT * FROM `user-table` where email=:email")
    fun fetchUserByEmail(email:String): Flow<UserEntity>

    @Query("SELECT EXISTS(SELECT * FROM `user-table` WHERE email = :email)")
    fun isEmailExist(email: String ) : Boolean

    @Query("UPDATE `user-table` SET resume = :resume,resumeName = :resumeName,resumeStatus = :resumeStatus WHERE id =:id")
    fun update(resume: String?,resumeName:String?,resumeStatus:String?, id: Int)
}
