package com.example.klapp_notes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface TableDao {
    @Insert
    suspend fun insertTable(table: Table): Long

    @Query("SELECT * FROM tables WHERE workspace_id = :workspaceId")
    suspend fun getTablesByWorkspace(workspaceId: Int): List<Table>
}
