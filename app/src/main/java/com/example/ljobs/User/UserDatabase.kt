package com.example.ljobs.User

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ljobs.Edu.EduDao
import com.example.ljobs.Edu.EduEntity
import com.example.ljobs.Edu.LanguageDao
import com.example.ljobs.Edu.LanguageEntity
import com.example.ljobs.Job.JobDao
import com.example.ljobs.Job.JobEntity


@Database(entities = [UserEntity::class,EduEntity::class,LanguageEntity::class, JobEntity::class], version = 10)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun eduDao(): EduDao
    abstract fun languageDao(): LanguageDao
    abstract fun jobDao(): JobDao
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