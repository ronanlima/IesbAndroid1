package br.ronanlima.opiniaodetudo.data

import br.ronanlima.opiniaodetudo.model.Review
import java.util.*

/**
 * Created by rlima on 17/09/19.
 */
class ReviewRepository {
    private constructor()

    companion object {
        val instance: ReviewRepository = ReviewRepository()
    }

    private val data = mutableListOf<Review>()

    fun save(opiniao: String) {
        data.add(Review(UUID.randomUUID().toString(), opiniao))
    }

    fun listAll() : List<Review> {
        return data.toList()
    }

    fun getByPosition(posicao: Int) : Review {
        return data.get(posicao)
    }
}