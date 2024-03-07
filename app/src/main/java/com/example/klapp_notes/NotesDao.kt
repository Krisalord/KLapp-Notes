package com.example.klapp_notes

import androidx.room.*

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes_table")
    fun getAll(): List<Notes>


    @Query("SELECT * FROM notes_table WHERE note_name LIKE :roll LIMIT 1")
    suspend fun findByRoll(roll: Int): Notes

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Notes)


    @Delete
    suspend fun delete(note: Notes)

    @Query("DELETE FROM Notes_table")
    suspend fun deleteAll()


}