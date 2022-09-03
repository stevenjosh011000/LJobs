package com.example.ljobs


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
    fun fetchAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM `user-table` where id=:id")
    fun fetchUserById(id:Int): Flow<UserEntity>

    @Query("SELECT * FROM `user-table` where email=:email")
    fun fetchUserByEmail(email:String): Flow<UserEntity>

    @Query("SELECT EXISTS(SELECT * FROM `user-table` WHERE email = :email)")
    fun isEmailExist(email: String ) : Boolean
}
