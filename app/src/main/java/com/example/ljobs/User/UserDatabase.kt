package com.example.ljobs.User

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ljobs.Edu.EduDao
import com.example.ljobs.Edu.EduEntity
import com.example.ljobs.Edu.LanguageDao
import com.example.ljobs.Edu.LanguageEntity


@Database(entities = [UserEntity::class,EduEntity::class,LanguageEntity::class], version = 9)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun eduDao(): EduDao
    abstract fun languageDao(): LanguageDao

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