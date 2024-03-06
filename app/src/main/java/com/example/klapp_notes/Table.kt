package com.example.klapp_notes

// Table.kt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tables")
data class Table(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var tableName: String = "" // Add a field for the table name
    // Add other properties as needed
)
