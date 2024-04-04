package com.example.klapp_notes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface TableDao {
    @Insert
    suspend fun insert(table: Table): Long

    @Query("SELECT * FROM tables WHERE workspace_id = :workspaceId")
    suspend fun getTablesByWorkspace(workspaceId: Int): List<Table>

    @Query("DELETE FROM tables WHERE id = :tableId")
    suspend fun deleteById(tableId: Long)



}
