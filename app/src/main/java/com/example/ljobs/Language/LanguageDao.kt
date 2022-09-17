package com.example.ljobs.Edu

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LanguageDao {

    @Insert
    fun insert(languageEntity: LanguageEntity)

    @Query("UPDATE `language-table` SET english = :english,chinese = :chinese,bahasaMelayu = :bahasaMelayu, tamil = :tamil," +
            "cantonese = :cantonese, hakka = :hakka, hokkien = :hokkien, hindi = :hindi WHERE email =:email")
    fun update(english: Int,chinese: Int,bahasaMelayu: Int,tamil: Int,cantonese: Int,hakka: Int,hokkien: Int,hindi: Int, email:String)

    @Query("SELECT * FROM `language-table` where email=:email")
    fun fetchAllLanByEmail(email:String): Flow<LanguageEntity>
}