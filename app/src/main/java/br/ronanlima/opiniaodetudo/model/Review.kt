package br.ronanlima.opiniaodetudo.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

/**
 * Created by rlima on 17/09/19.
 */
@Entity
data class Review(
        @PrimaryKey val id: String,
        var opiniao: String,
        var titulo: String,
        @ColumnInfo(name = "photo_path")
        val photoPath: String? = null,
        @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
        val thumbnail: ByteArray? = null) : Serializable {

    @Ignore
    constructor(id: String, opiniao: String, titulo: String) : this(id, opiniao, titulo, null, null)
}