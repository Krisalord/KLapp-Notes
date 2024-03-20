package com.example.klapp_notes

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Workspace::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workspaceDao(): WorkspaceDao
}