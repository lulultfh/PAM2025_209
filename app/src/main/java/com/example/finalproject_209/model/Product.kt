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
    val isEntryValid: Boolean = true,
    val validation: ValidasiProductField = ValidasiProductField()
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
data class ValidasiProductField(
    val errorNama: String? = null,
    val errorPrice: String? = null,
    val errorDesc: String? = null,
    val errorStok: String? = null,
    val errorKategori: String? = null,
    val errorImage: String? = null
) {
    val isValid: Boolean
        get() = listOf(errorNama, errorPrice, errorDesc, errorStok, errorKategori, errorImage)
            .all { it == null }
}

fun validasiInputPerField(product: DetailProduct): ValidasiProductField {
    return ValidasiProductField(
        errorNama = when {
            product.nama.isBlank() -> "Nama product wajib diisi"
            product.nama.length < 3 -> "Nama product minimal 3 karakter"
            !product.nama.all { it.isLetter() || it.isWhitespace() } -> "Nama hanya boleh huruf dan spasi"
            else -> null
        },
        errorPrice = if (product.price <= 0) "Harga harus lebih dari 0" else null,
        errorDesc = if (product.desc.isBlank()) "Description wajib diisi" else null,
        errorStok = if (product.stok < 0 ) "Stok tidak boleh negatif" else null,
        errorKategori = null,
        errorImage = if (product.image.isBlank())"Gambar wajib diisi" else null
    )
}