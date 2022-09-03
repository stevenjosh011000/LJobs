package com.example.ljobs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [UserEntity::class], version = 3)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao():UserDao

    companion object{

        @Volatile
        private var INSTANCE : UserDatabase? = null

        fun getDatabase(context: Context):UserDatabase{
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