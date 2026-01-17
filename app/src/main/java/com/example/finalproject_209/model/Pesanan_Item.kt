package com.example.finalproject_209.model

import kotlinx.serialization.Serializable

@Serializable
data class DataPesananItem(
    val id: Int,
    val pesanan_id: Int, //foreignkey
    val product_id: Int, //foreignkey
    val qty: Int,
    val subtotal: Int
)

data class UiStatePesananItem(
    val detailItemPesanan: DetailItemPesanan = DetailItemPesanan(),
    val isEntryValid: Boolean = true
)
data class DetailItemPesanan(
    val id: Int = 0,
    val pesanan_id: Int = 0,
    val product_id: Int = 0,
    val qty: Int = 0,
    val subtotal: Int = 0
)
fun DetailItemPesanan.toDataPesananItem(): DataPesananItem = DataPesananItem(
    id = id,
    pesanan_id = pesanan_id,
    product_id = product_id,
    qty = qty,
    subtotal = subtotal
)
fun DataPesananItem.toUiStatePesananItem(isEntryValid: Boolean = false): UiStatePesananItem =
    UiStatePesananItem(
    detailItemPesanan =  this.toDetailItemPesanan(),
    isEntryValid = isEntryValid
)

fun DataPesananItem.toDetailItemPesanan(): DetailItemPesanan= DetailItemPesanan(
    id = id,
    pesanan_id = pesanan_id,
    product_id = product_id,
    qty = qty,
    subtotal = subtotal
)