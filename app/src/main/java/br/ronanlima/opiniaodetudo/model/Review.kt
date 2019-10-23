package br.ronanlima.opiniaodetudo.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

/**
 * Created by rlima on 17/09/19.
 */
@Entity
data class Review(@PrimaryKey val id: String, var opiniao: String) : Serializable