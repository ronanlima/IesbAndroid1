package br.ronanlima.opiniaodetudo.infra.dao.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.ronanlima.opiniaodetudo.infra.dao.ReviewTableInfo
import br.ronanlima.opiniaodetudo.model.Review

/**
 * Created by rlima on 08/10/19.
 */
@Database(entities = arrayOf(Review::class), version = 4)
abstract class ReviewDatabase : RoomDatabase() {

    companion object {
        private var instance: ReviewDatabase? = null

        fun getInstance(context: Context): ReviewDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, ReviewDatabase::class.java, "review_database")
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                        .build()
            }
            return instance!!
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ${ReviewTableInfo.TABLE_NAME} ADD COLUMN ${ReviewTableInfo.COLUMN_TITULO} TEXT NOT NULL DEFAULT 'Não informado'")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ${ReviewTableInfo.TABLE_NAME} ADD COLUMN ${ReviewTableInfo.COLUMN_PHOTO_PATH} TEXT")
                database.execSQL("ALTER TABLE ${ReviewTableInfo.TABLE_NAME} ADD COLUMN ${ReviewTableInfo.COLUMN_THUMBNAIL} BLOB")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ${ReviewTableInfo.TABLE_NAME} ADD COLUMN ${ReviewTableInfo.COLUMN_LATITUDE} REAL")
                database.execSQL("ALTER TABLE ${ReviewTableInfo.TABLE_NAME} ADD COLUMN ${ReviewTableInfo.COLUMN_LONGITUDE} REAL")
            }
        }
    }

    abstract fun reviewDAO(): ReviewDAO
}