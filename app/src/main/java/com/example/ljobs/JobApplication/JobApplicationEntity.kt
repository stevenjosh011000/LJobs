package com.example.ljobs.JobApplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.ljobs.Job.JobEntity
import java.util.*

@Entity(tableName = "application-table",
    foreignKeys = [ForeignKey(
        entity = JobEntity::class,
        childColumns = ["jobId"],
        parentColumns = ["id"]
    )])
data class JobApplicationEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "jobId")
    val jobId: Int? = null,
    @ColumnInfo(name = "email")
    val email: String? = null,
    @ColumnInfo(name = "additionalInfo")
    val additionalInfo: String? = null,
    @ColumnInfo(name = "workingExp")
    val workingExp: String? = null,
    @ColumnInfo(name = "status")
    val status: String? = null
)