package br.ronanlima.opiniaodetudo.infra.dao.room

import android.database.Cursor
import androidx.room.*
import br.ronanlima.opiniaodetudo.infra.dao.ReviewTableInfo
import br.ronanlima.opiniaodetudo.model.Review

/**
 * Created by rlima on 08/10/19.
 */

@Dao
interface ReviewDAO {

    @Insert
    fun insert(review: Review)

    @Update
    fun update(review: Review)

    @Query("SELECT * FROM ${ReviewTableInfo.TABLE_NAME}")
    fun readAll(): Cursor

    @Delete
    fun delete(review: Review)
}