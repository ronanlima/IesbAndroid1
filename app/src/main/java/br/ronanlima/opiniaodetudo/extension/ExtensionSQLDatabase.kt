package br.ronanlima.opiniaodetudo.extension

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

/**
 * Created by rlima on 24/09/19.
 */
//fun SQLiteDatabase.selectAll(tableName: String, columns: Array<String>, selection: String? = null): Cursor {
//    return this.query(tableName, columns, selection, null, null, null, null)
//}

fun SQLiteDatabase.selectAll(tableName: String, columns: Array<String>, selection: String? = null, selectionArgs: Array<String>? = null, groupBy: String? = null, having: String? = null, orderBy: String? = null): Cursor {
    return this.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy)
}