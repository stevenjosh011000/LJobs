package com.example.ljobs.Edu

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "edu-table")
data class EduEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    @ColumnInfo(name = "Email")
    val email : String? = null,
    @ColumnInfo(name = "Education Level")
    val eduLvl : String? = null,
    @ColumnInfo(name = "Certificate Name")
    val eduCert : String? = null,
    @ColumnInfo(name = "School")
    val eduSchool : String? = null,
    @ColumnInfo(name = "End Year")
    val eduEndYear : String? = null,
)