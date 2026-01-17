package com.example.finalproject_209.repository

import com.example.finalproject_209.model.DataProduct
import com.example.finalproject_209.service.ServiceApiBakery
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface RepositoryDataProduct{
    suspend fun getDataProduct(): List<DataProduct>
    suspend fun getDetailProduct(id: Int): DataProduct
//    suspend fun tambahProduct(dataProduct: DataProduct):retrofit2.Response<Void>
    suspend fun editProduct(id: Int, dataProduct: DataProduct):retrofit2.Response<Void>
    suspend fun hapusProduct(id: Int): retrofit2.Response<Void>
    suspend fun tambahProductMultipart(
        nama: RequestBody, price: RequestBody, desc: RequestBody,
        stok: RequestBody, kategori: RequestBody, image: MultipartBody.Part
    ): retrofit2.Response<Void>
    suspend fun updateProductMultipart(
        id: Int,
        nama: RequestBody, price: RequestBody, desc: RequestBody,
        stok: RequestBody, kategori: RequestBody, image: MultipartBody.Part?
    ): retrofit2.Response<Void>
}

class NetworkProductRepo(
    private val serviceApiBakery: ServiceApiBakery
): RepositoryDataProduct{
    override suspend fun getDataProduct(): List<DataProduct> = serviceApiBakery.getAllProduct()
    override suspend fun getDetailProduct(id: Int): DataProduct = serviceApiBakery.getProductById(id)
//    override suspend fun tambahProduct(dataProduct: DataProduct): Response<Void> = serviceApiBakery.tambahProduct(dataProduct)
    override suspend fun editProduct(id: Int, dataProduct: DataProduct): Response<Void> = serviceApiBakery.updateProduct(id, dataProduct)
    override suspend fun hapusProduct(id: Int): Response<Void> = serviceApiBakery.hapusProduct(id)
    override suspend fun tambahProductMultipart(
        nama: RequestBody, price: RequestBody, desc: RequestBody,
        stok: RequestBody, kategori: RequestBody, image: MultipartBody.Part
    ): Response<Void> = serviceApiBakery.tambahProductMultipart(nama, price, desc, stok, kategori, image)
    override suspend fun updateProductMultipart(
        id: Int, nama: RequestBody, price: RequestBody, desc: RequestBody,
        stok: RequestBody, kategori: RequestBody, image: MultipartBody.Part?
    ): retrofit2.Response<Void> = serviceApiBakery.updateProductMultipart(
        id, nama, price, desc, stok, kategori, image
    )
}