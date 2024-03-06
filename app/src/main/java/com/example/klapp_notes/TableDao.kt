package com.example.klapp_notes

// TableDao.kt
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TableDao {
    @Insert
    suspend fun insertTable(table: Table)

    @Query("SELECT * FROM tables")
    suspend fun getAllTables(): List<Table>

    @Query("DELETE FROM tables WHERE id = :tableId")
    suspend fun deleteTableById(tableId: Int)
}



