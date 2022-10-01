package com.example.ljobs.User


import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.sql.Blob

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

    @Query("SELECT * FROM `user-table` where id=:id")
    fun fetchById(id:Int): UserEntity

    @Query("UPDATE `user-table` SET image = :image WHERE email = :email")
    fun updateImage(image: ByteArray, email: String)

    @Query("UPDATE `user-table` SET image = :image, email = :email,name=:name,`Phone Num`= :phoneNum, role =:role WHERE id = :id")
    fun updateUserProfile(image: ByteArray?, email: String?, name: String?, phoneNum: String?,role:String?, id: Int)

    @Query("DELETE FROM `user-table` WHERE id = :id")
    fun deleteUserById(id: Int)

    @Query("SELECT * FROM `user-table` where Role='3'")
    fun fetchManager(): UserEntity



}
