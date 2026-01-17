package com.example.finalproject_209.repository

import com.example.finalproject_209.model.DataPesanan
import com.example.finalproject_209.model.DataProduct
import com.example.finalproject_209.service.ServiceApiBakery
import okhttp3.ResponseBody
import retrofit2.Response

interface RepositoryDataPesanan{
    suspend fun getAllPesanan(): List<DataPesanan>
    suspend fun getPesananById(id: Int): DataPesanan
    suspend fun tambahPesanan(dataPesanan: DataPesanan):retrofit2.Response<Void>
    suspend fun editPesanan(id: Int, dataPesanan: DataPesanan):retrofit2.Response<Void>
    suspend fun hapusPesanan(id: Int):retrofit2.Response<Void>
    suspend fun updateNamaCust(id: Int, namaCust: String): retrofit2.Response<Void>
    suspend fun printPesanan(id: Int): retrofit2.Response<ResponseBody>
}

class NetworkPesananRepo(
    private val serviceApiBakery: ServiceApiBakery
): RepositoryDataPesanan{
    override suspend fun getAllPesanan(): List<DataPesanan> = serviceApiBakery.getAllPesanan()
    override suspend fun getPesananById(id: Int): DataPesanan = serviceApiBakery.getPesananById(id)
    override suspend fun tambahPesanan(dataPesanan: DataPesanan): Response<Void> = serviceApiBakery.tambahPesanan(dataPesanan)
    override suspend fun editPesanan(id: Int, dataPesanan: DataPesanan): Response<Void> = serviceApiBakery.updatePesanan(id, dataPesanan)
    override suspend fun hapusPesanan(id: Int): Response<Void> = serviceApiBakery.hapusPesanan(id)
    override suspend fun updateNamaCust(id: Int, namaCust: String): Response<Void> = serviceApiBakery.updateNamaCust(id, namaCust = mapOf("namaCust" to namaCust))
    override suspend fun printPesanan(id: Int): Response<ResponseBody> = serviceApiBakery.printPesanan(id)
}