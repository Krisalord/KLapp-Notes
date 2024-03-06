package com.example.klapp_notes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Notes::class, Table::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
    abstract fun tableDao(): TableDao

    companion object {
        private const val CURRENT_VERSION = 1

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Use this to allow destructive migrations
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
