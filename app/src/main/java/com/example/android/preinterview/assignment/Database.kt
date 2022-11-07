package com.example.android.preinterview.assignment

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        MarvelEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun marvelDao(): MarvelDao
}