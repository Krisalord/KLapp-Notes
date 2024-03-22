package com.example.klapp_notes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "tables")
class Table(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "table_name") val tableName: String,
    @ColumnInfo(name = "workspace_id") val workspaceId: Int // New field for workspace ID
)
