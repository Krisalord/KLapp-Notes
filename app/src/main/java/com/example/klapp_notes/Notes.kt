package com.example.klapp_notes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notes_table")
data class Notes(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "note_name") val noteName: String?
)
