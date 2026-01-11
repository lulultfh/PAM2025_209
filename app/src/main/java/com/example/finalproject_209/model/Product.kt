package com.example.finalproject_209.model

import kotlinx.serialization.Serializable

@Serializable
data class DataProduct(
    val id: Int,
    val nama: String,
    val price: Int,
    val desc: String,
    val stok: Int,
    val kategori: Kategori,
    val image: String
){
    fun doesMatchSearchQuery(query: String): Boolean{
        val matchingFields = listOf(
            id.toString(),
            nama,
            price.toString(),
            desc,
            stok.toString(),
            kategori.name,
            image
        )
        return matchingFields.any {
            it.contains(query, ignoreCase = true)
        }
    }
}

@Serializable
enum class Kategori {
    cake,
    pastry,
    bread,
    cookies
}

data class UiStateProduct(
    val detailProduct: DetailProduct = DetailProduct(),
    val isEntryValid: Boolean = true
)

data class DetailProduct(
    val id: Int = 0,
    val nama: String = "",
    val price: Int = 0,
    val desc: String = "",
    val stok: Int = 0,
    val kategori: Kategori = Kategori.cake,
    val image: String = ""
)

fun DetailProduct.toDataProduct(): DataProduct = DataProduct(
    id = id,
    nama = nama,
    price = price,
    desc = desc,
    stok = stok,
    kategori = kategori,
    image = image
)

fun DataProduct.toUiStateProduct(isEntryValid: Boolean = false): UiStateProduct = UiStateProduct(
    detailProduct = this.toDetailProduct(),
    isEntryValid = isEntryValid
)

fun DataProduct.toDetailProduct(): DetailProduct = DetailProduct(
    id = id,
    nama = nama,
    price = price,
    desc = desc,
    stok = stok,
    kategori = kategori,
    image = image
)