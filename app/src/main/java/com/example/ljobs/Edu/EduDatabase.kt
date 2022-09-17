package com.example.ljobs.Edu

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EduEntity::class], version = 1)
abstract class EduDatabase : RoomDatabase(){
    abstract fun eduDao(): EduDao

    companion object{

        @Volatile
        private var INSTANCE : EduDatabase? = null

        fun getDatabase(context: Context): EduDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EduDatabase::class.java,
                    "edu-database"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}