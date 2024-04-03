package com.example.klapp_notes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkspaceDao {
    @Insert
    suspend fun insert(workspace: Workspace): Long

    @Query("SELECT * FROM workspaces")
    suspend fun getAllWorkspaces(): List<Workspace>
}