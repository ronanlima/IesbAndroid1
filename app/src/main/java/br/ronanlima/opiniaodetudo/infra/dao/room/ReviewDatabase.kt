package br.ronanlima.opiniaodetudo.infra.dao.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import br.ronanlima.opiniaodetudo.model.Review

/**
 * Created by rlima on 08/10/19.
 */
@Database(entities = arrayOf(Review::class), version = 1)
abstract class ReviewDatabase : RoomDatabase() {

    companion object {
        private var instance: ReviewDatabase? = null

        fun getInstance(context: Context): ReviewDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, ReviewDatabase::class.java, "review_database")
//                        .allowMainThreadQueries()
                        .build()
            }
            return instance!!
        }
    }

    abstract fun reviewDAO(): ReviewDAO
}