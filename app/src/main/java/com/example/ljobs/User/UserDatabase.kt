package com.example.ljobs.User

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ljobs.Edu.EduDao
import com.example.ljobs.Edu.EduEntity
import com.example.ljobs.Job.JobDao
import com.example.ljobs.Job.JobEntity
import com.example.ljobs.JobApplication.JobApplicationDao
import com.example.ljobs.JobApplication.JobApplicationEntity


@Database(entities = [UserEntity::class,EduEntity::class, JobEntity::class, JobApplicationEntity::class], version = 22)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun eduDao(): EduDao
    abstract fun jobDao(): JobDao
    abstract fun jobApplicationDao(): JobApplicationDao

    companion object{

        @Volatile
        private var INSTANCE : UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "users-database"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }


    }
}