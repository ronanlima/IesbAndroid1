package br.ronanlima.opiniaodetudo.infra.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by rlima on 24/09/19.
 */
class OpiniaoTudoDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {

        const val DATABASE_NAME = "opiniao_tudo_database"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE ${ReviewTableInfo.TABLE_NAME} ("
                + "${ReviewTableInfo.COLUMN_ID} TEXT PRIMARY KEY, " + "${ReviewTableInfo.COLUMN_REVIEW} TEXT NOT NULL)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }
}