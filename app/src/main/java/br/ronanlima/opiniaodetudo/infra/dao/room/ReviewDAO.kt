package br.ronanlima.opiniaodetudo.infra.dao.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.database.Cursor
import br.ronanlima.opiniaodetudo.infra.dao.ReviewTableInfo
import br.ronanlima.opiniaodetudo.model.Review

/**
 * Created by rlima on 08/10/19.
 */

@Dao
interface ReviewDAO {

    @Insert
    fun insert(review: Review)

    @Query("SELECT * FROM ${ReviewTableInfo.TABLE_NAME}")
    fun readAll(): Cursor

    @Delete
    fun delete(review: Review)
}