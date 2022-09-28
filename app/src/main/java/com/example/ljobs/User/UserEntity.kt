package com.example.ljobs.User


import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob

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
    val resume : String? = "",
    @ColumnInfo(name = "ResumeName")
    val resumeName : String? = "",
    @ColumnInfo(name = "ResumeStatus")
    val resumeStatus : String? = "0",
    @ColumnInfo(name = "Role")
    val role : String? = "1",
    @ColumnInfo(name = "Image")
    val image: ByteArray? = null
    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEntity

        if (id != other.id) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (name != other.name) return false
        if (phoneNum != other.phoneNum) return false
        if (resume != other.resume) return false
        if (resumeName != other.resumeName) return false
        if (resumeStatus != other.resumeStatus) return false
        if (role != other.role) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (password?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (phoneNum?.hashCode() ?: 0)
        result = 31 * result + (resume?.hashCode() ?: 0)
        result = 31 * result + (resumeName?.hashCode() ?: 0)
        result = 31 * result + (resumeStatus?.hashCode() ?: 0)
        result = 31 * result + (role?.hashCode() ?: 0)
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}
