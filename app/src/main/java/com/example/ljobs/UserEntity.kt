package com.example.ljobs

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user-table")
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    @ColumnInfo(name = "Email")
    val email : String? = null,
    @ColumnInfo(name = "Password")
    val password : String? = null,
    @ColumnInfo(name = "Name")
    val name : String? = null,
    @ColumnInfo(name = "Phone Num")
    val phoneNum : String? = null,
    @ColumnInfo(name = "Resume")
    val resume : String? = null,
    @ColumnInfo(name = "Role")
    val role : String? = "1",

    )
