package com.example.ljobs.Job

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "job-table")
data class JobEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    @ColumnInfo(name = "email")
    val email: String? = null,
    @ColumnInfo(name = "title")
    val title: String? = null,
    @ColumnInfo(name = "eduRequirement")
    val eduRequirement: String? = null,
    @ColumnInfo(name = "salary")
    val salary: String? = null,
    @ColumnInfo(name = "descrip")
    val descrip: String? = null,
    @ColumnInfo(name = "location")
    val location: String? = null,

    @ColumnInfo(name = "type")
    val type: String? = null,

    @ColumnInfo(name = "companyInfo")
    val companyInfo: String? = null,
    @ColumnInfo(name = "jobStatus")
    val jobStatus : String? = "0",
)