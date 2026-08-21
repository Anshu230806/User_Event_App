package com.example.eventsapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.eventsapp.data.repository.source.local.dao.EventDao
import com.example.eventsapp.data.repository.source.local.dao.UserDao
import com.example.eventsapp.data.repository.source.local.entity.EventEntity
import com.example.eventsapp.data.repository.source.local.entity.UserEntity

//  Note that exportSchema should be true in production databases.
//  in production is recommended to track schema history in your version control system.
// @Database represents that it is the roomDatabase , entities represent all tables,
// and version means the current version of schema  if schema is changed then this number incremented ,

@Database(
    entities = [EventEntity::class, UserEntity::class],
    version = 2,
    exportSchema = false
)

// this class must be abstract bcz . Room will automatically generate the underlying implementation for you at compile time.
// @Database. It then automatically creates a generated subclass (e.g., AppDatabase_Impl) that writes all the complex, repetitive SQLite boilerplates needed to handle your data.
abstract class EventDatabase: RoomDatabase(){
    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
}