package com.example.aksharadeepa.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "questions",
    foreignKeys = [ForeignKey(
        entity = Chapter::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("chapterId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val chapterId: Int,
    val text: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOptionIndex: Int // 0 for A, 1 for B, 2 for C, 3 for D
)
