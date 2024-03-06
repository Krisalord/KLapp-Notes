package com.example.klapp_notes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workspaces")
class Workspace (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String
)

