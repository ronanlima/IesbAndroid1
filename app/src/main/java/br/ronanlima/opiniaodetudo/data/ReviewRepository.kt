package br.ronanlima.opiniaodetudo.data

import android.content.Context
import br.ronanlima.opiniaodetudo.infra.dao.room.ReviewDAO
import br.ronanlima.opiniaodetudo.infra.dao.room.ReviewDatabase
import br.ronanlima.opiniaodetudo.model.Review
import java.util.*

/**
 * Created by rlima on 17/09/19.
 */
class ReviewRepository {
    private val reviewDAO: ReviewDAO

    constructor(context: Context) {
        val reviewDatabase = ReviewDatabase.getInstance(context)
        reviewDAO = reviewDatabase.reviewDAO()
    }

    private val data = mutableListOf<Review>()

    fun save(opiniao: String, titulo: String, path: String?, thumbnailBytes: ByteArray?): Review {
        val entity = Review(UUID.randomUUID().toString(), opiniao, titulo, path, thumbnailBytes)
        reviewDAO.insert(entity)
        return entity
    }

    fun update(review: Review) {
        reviewDAO.update(review)
    }

    fun updateLocation(entity: Review, latitude: Double, longitude: Double) {
        entity.latitude = latitude
        entity.longitude = longitude
        reviewDAO.update(entity)
    }

    fun listAll(): List<Review> {
        val cursor = reviewDAO.readAll()
        while (cursor.moveToNext()) {
            data.add(Review(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getBlob(4), cursor.getDouble(5), cursor.getDouble(6)))
        }
        return data.toList()
    }

    fun getByPosition(posicao: Int): Review {
        return data.get(posicao)
    }

    fun delete(review: Review): Unit {
        return reviewDAO.delete(review)
    }
}