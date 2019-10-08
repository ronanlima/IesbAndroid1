package br.ronanlima.opiniaodetudo.infra.dao

//import br.ronanlima.opiniaodetudo.extension.SQLiteDatabaseTools
//import br.ronanlima.opiniaodetudo.extension.SQLiteDatabaseTools.selectAll
//import br.ronanlima.opiniaodetudo.extension.selectAll
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.ronanlima.opiniaodetudo.extension.selectAll
import br.ronanlima.opiniaodetudo.model.Review
import java.util.*

/**
 * Created by rlima on 24/09/19.
 */
class ReviewDAOSQLite {
    private val dbHelper: SQLiteOpenHelper

    constructor(context: Context) {
        this.dbHelper = OpiniaoTudoDBHelper(context)
    }

    fun save(review: String) {
        val writableDatabase = dbHelper.writableDatabase
        writableDatabase.insert(ReviewTableInfo.TABLE_NAME, null, ContentValues().apply {
            put(ReviewTableInfo.COLUMN_ID, UUID.randomUUID().toString())
            put(ReviewTableInfo.COLUMN_REVIEW, review)
        })
        writableDatabase.close()
    }

    fun list(): List<Review> {
        val readableDatabase = dbHelper.readableDatabase
        val cursor = readableDatabase.selectAll(ReviewTableInfo.TABLE_NAME, arrayOf(ReviewTableInfo.COLUMN_ID, ReviewTableInfo.COLUMN_REVIEW))
        val reviews = mutableListOf<Review>()
        while (cursor.moveToNext()) {
            reviews.add(createReview(cursor))
        }
        readableDatabase.close()
        return reviews
    }

    fun createReview(cursor: Cursor): Review {
        val id = cursor.getString(0)
        val review = cursor.getString(1)
        return Review(id, review)
    }

    fun SQLiteDatabase.selectAll(columns: Array<String>): Cursor {
        return this.query(ReviewTableInfo.TABLE_NAME, columns, null, null, null, null, null)
    }

}