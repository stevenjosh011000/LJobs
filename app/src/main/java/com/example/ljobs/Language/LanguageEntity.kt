package com.example.ljobs.Edu

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "language-table")
data class LanguageEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    @ColumnInfo(name = "Email")
    val email : String? = null,
    @ColumnInfo(name = "English")
    val english : Int = 0,
    @ColumnInfo(name = "Chinese")
    val chinese : Int = 0,
    @ColumnInfo(name = "BahasaMelayu")
    val bahasaMelayu : Int = 0,
    @ColumnInfo(name = "Tamil")
    val tamil : Int = 0,
    @ColumnInfo(name = "Cantonese")
    val cantonese : Int = 0,
    @ColumnInfo(name = "Hakka")
    val hakka : Int = 0,
    @ColumnInfo(name = "Hokkien")
    val hokkien : Int = 0,
    @ColumnInfo(name = "Hindi")
    val hindi : Int = 0,
)