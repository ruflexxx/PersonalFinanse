package com.example.personalfinance.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseProvider {
    private var database: FinanceDatabase? = null

    private val MIGRATION_1_2 = object : Migration(1,2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                     ALTER TABLE expenses
                     ADD COLUMN dateTime INTEGER NOT NULL DEFAULT 0
                     """
            )
        }
    }

    fun getDatabase(context: Context): FinanceDatabase {
        if (database == null) {
            database = Room.databaseBuilder(
                context,
                FinanceDatabase::class.java,
                "finance_database"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
        }
        return database!!
    }
}