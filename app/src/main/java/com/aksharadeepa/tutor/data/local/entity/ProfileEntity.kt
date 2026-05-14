package com.aksharadeepa.tutor.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_profile")
data class ProfileEntity(
    @PrimaryKey val id: Int = 1,
    val studentName: String,
    val schoolName: String,
    val medium: String,
    val targetPercentage: Int,
    val preferredSubjectId: Int = 1
)
