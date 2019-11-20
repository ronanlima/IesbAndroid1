package br.ronanlima.opiniaodetudo.infra.dao.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import br.ronanlima.opiniaodetudo.model.Review

/**
 * Created by rlima on 08/10/19.
 */
@Database(entities = arrayOf(Review::class), version = 2)
abstract class ReviewDatabase : RoomDatabase() {

    companion object {
        private var instance: ReviewDatabase? = null

        fun getInstance(context: Context): ReviewDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, ReviewDatabase::class.java, "review_database")
                        .addMigrations()
                        .addMigrations(MIGRATION_1_2)
                        .build()
            }
            return instance!!
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Review ADD COLUMN titulo TEXT NOT NULL DEFAULT 'Não informado'")
            }
        }
    }

    abstract fun reviewDAO(): ReviewDAO
}