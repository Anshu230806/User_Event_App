
package com.example.eventsapp.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.eventsapp.data.repository.source.local.dao.EventDao
import com.example.eventsapp.data.repository.source.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//private val MIGRATION_2_3 = object : Migration(2, 3) {
//    override fun migrate(db: SupportSQLiteDatabase) {
//        db.execSQL("ALTER TABLE Users ADD COLUMN email TEXT")
//        db.execSQL("ALTER TABLE Users ADD COLUMN role TEXT")
//        db.execSQL("ALTER TABLE Users ADD COLUMN fullName TEXT")
//    }
//}
private val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE events ADD COLUMN clubName TEXT")
        db.execSQL("ALTER TABLE events ADD COLUMN registrationDeadline INTEGER")
    }
}
// .addMigrations(MIGRATION_2_3, MIGRATION_3_4)

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideEventDatabase(
        @ApplicationContext context: Context
    ): EventDatabase {
        return Room.databaseBuilder(
            context,
            EventDatabase::class.java,
            "events.db"
        )
            .addMigrations(MIGRATION_3_4)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideEventDao(database: EventDatabase): EventDao {
        return database.eventDao()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: EventDatabase): UserDao {
        return database.userDao()
    }
}
