package com.example.finalproject_209.repository

import android.app.Application
import com.example.finalproject_209.service.ServiceApiBakery
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface ContainerApp{
    val repositoryDataUser: RepositoryDataUser
    val repositoryDataProduct: RepositoryDataProduct
    val repositoryDataPesanan: RepositoryDataPesanan
    val repositoryDataItemPesanan: RepositoryDataItemPesanan
}

class BakeryContainer: ContainerApp{
    private val baseURL = "http://10.0.2.2:3000/"
    val imageUrl = baseURL + "product/image/"
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseURL).build()
    private val bakeryService: ServiceApiBakery by lazy { retrofit.create(ServiceApiBakery::class.java)}

    override val repositoryDataUser: RepositoryDataUser by lazy { NetworkUserRepo(bakeryService) }
    override val repositoryDataProduct: RepositoryDataProduct by lazy { NetworkProductRepo(bakeryService) }
    override val repositoryDataPesanan: RepositoryDataPesanan by lazy { NetworkPesananRepo(bakeryService) }
    override val repositoryDataItemPesanan: RepositoryDataItemPesanan by lazy { NetworkItemPesananRepo(bakeryService) }
}

class WhimsyWhisk: Application() {
    lateinit var container: ContainerApp
    override fun onCreate() {
        super.onCreate()
        container = BakeryContainer()
    }
}
