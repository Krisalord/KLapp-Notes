package com.example.klapp_notes

// TableDao.kt
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TableDao {
    @Insert
    suspend fun insertTable(table: Table)

    @Query("SELECT * FROM tables")
    suspend fun getAllTables(): List<Table>

    @Query("SELECT * FROM tables WHERE id = :tableId")
    suspend fun getTableById(tableId: Int): Table?

    @Update
    suspend fun updateTable(table: Table)

    @Query("DELETE FROM tables WHERE id = :tableId")
    suspend fun deleteTableById(tableId: Int)
}

