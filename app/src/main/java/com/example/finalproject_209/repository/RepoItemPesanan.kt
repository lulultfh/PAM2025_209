package com.example.finalproject_209.repository

import com.example.finalproject_209.model.DataPesananItem
import com.example.finalproject_209.service.ServiceApiBakery
import retrofit2.Response

interface RepositoryDataItemPesanan{
    suspend fun getAllItemPesanan(): List<DataPesananItem>
    suspend fun getItemPesananById(id: Int): DataPesananItem
    suspend fun tambahItemPesanan(dataPesananItem: DataPesananItem):retrofit2.Response<Void>
    suspend fun editItemPesanan(id: Int, qty: Int, subtotal: Int):retrofit2.Response<Void>
    suspend fun hapusItemPesanan(id: Int):retrofit2.Response<Void>

//    suspend fun updateQty(id: Int, qty: Int, subtotal: Int): retrofit2.Response<Void>
}

class NetworkItemPesananRepo(
    private val serviceApiBakery: ServiceApiBakery
): RepositoryDataItemPesanan{
    override suspend fun getAllItemPesanan(): List<DataPesananItem> = serviceApiBakery.getAllItemPesanan()
    override suspend fun getItemPesananById(id: Int): DataPesananItem = serviceApiBakery.getItemPesananById(id)
    override suspend fun tambahItemPesanan(dataPesananItem: DataPesananItem): Response<Void> = serviceApiBakery.tambahItemPesanan(dataPesananItem)
    override suspend fun editItemPesanan(id: Int, qty: Int, subtotal: Int): Response<Void> {
        val body = mapOf("qty" to qty, "subtotal" to subtotal)
        return serviceApiBakery.updateItemPesanan(id, body)
    }
    override suspend fun hapusItemPesanan(id: Int): Response<Void> = serviceApiBakery.hapusItemPesanan(id)
//    Override suspend fun updateQty(id: Int, qty: Int, subtotal: Int): Response<Void> =
//        serviceApiBakery.updateQty(id, qty, subtotal)
}