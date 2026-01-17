package com.example.finalproject_209.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataPesanan(
    val id: Int,

    @SerialName("admin_id")
    val admin_id: Int, //foreignkey
    val tanggal: String,
    val total_harga: Int,
    val namaCust: String,
    val status: Status
)

@Serializable
enum class Status {
    finish,
    process
}

data class UiStatePesanan(
    val detailPesanan: DetailPesanan = DetailPesanan(),
    val isEntryValid: Boolean = true,
    val validation: ValidasiPesananField = ValidasiPesananField()
)
data class DetailPesanan(
    val id: Int = 0,
    val adminId: Int = 0,
    val tanggal: String = "",
    val total_harga: Int = 0,
    val namaCust: String = "",
    val status: Status = Status.process
)
fun DetailPesanan.toDataPesanan(): DataPesanan = DataPesanan(
    id = id,
    admin_id = adminId,
    tanggal = tanggal,
    total_harga = total_harga,
    namaCust = namaCust,
    status = status
)
fun DataPesanan.toUiStatePesanan(isEntryValid: Boolean = false): UiStatePesanan = UiStatePesanan(
    detailPesanan =  this.toDetailPesanan(),
    isEntryValid = isEntryValid
)

fun DataPesanan.toDetailPesanan(): DetailPesanan = DetailPesanan(
    id = id,
    adminId = admin_id,
    tanggal = tanggal,
    total_harga = total_harga,
    namaCust = namaCust,
    status = status
)

data class ValidasiPesananField(
    val errorNamaCust: String ?  = null,
    val errorTotalHarga: String ? = null,
    val errorAdmin : String ? = null
){
    val isValid: Boolean
        get() = listOf(
            errorNamaCust,
            errorTotalHarga,
            errorAdmin
        ).all { it == null }
}

fun validasiInputPesanan(detailPesanan: DetailPesanan): ValidasiPesananField{
    return ValidasiPesananField(
        errorNamaCust = when {
            detailPesanan.namaCust.isBlank() ->
                "Nama customer wajib diisi"
            detailPesanan.namaCust.length < 3 ->
                "Nama customer minimal 3 karakter"
            else -> null
        },
        errorTotalHarga = if (detailPesanan.total_harga <0) "Total harga harus lebih dari 0" else null,
        errorAdmin = if (detailPesanan.adminId <= 0) "Admin tidak valid" else null
    )
}