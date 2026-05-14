package com.example.aksharadeepa.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "chapters",
    foreignKeys = [ForeignKey(
        entity = Subject::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("subjectId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Chapter(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: Int,
    val title: String,
    val isCompleted: Boolean = false
)
