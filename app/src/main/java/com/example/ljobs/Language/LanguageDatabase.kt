package com.example.ljobs.Edu

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LanguageEntity::class], version = 1)
abstract class LanguageDatabase : RoomDatabase(){
    abstract fun languageDao(): LanguageDao

    companion object{

        @Volatile
        private var INSTANCE : LanguageDatabase? = null

        fun getDatabase(context: Context): LanguageDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LanguageDatabase::class.java,
                    "language-database"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}