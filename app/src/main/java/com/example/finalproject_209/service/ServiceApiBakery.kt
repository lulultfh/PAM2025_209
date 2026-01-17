package com.example.finalproject_209.service

import com.example.finalproject_209.model.DataPesanan
import com.example.finalproject_209.model.DataPesananItem
import com.example.finalproject_209.model.DataProduct
import com.example.finalproject_209.model.DataUser
import com.example.finalproject_209.model.LoginRequest
import com.example.finalproject_209.model.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApiBakery{
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )

//    Login methode //
    @GET("user")
    suspend fun getUser(): List<DataUser>
    @GET("user/{id}")
    suspend fun getUserById(@Query("id") id: Int): DataUser

    @POST("user/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
//    Product methode //
    @GET("product")
    suspend fun  getAllProduct(): List<DataProduct>

    @GET("product/{id}")
    suspend fun getProductById(@Path("id") id: Int): DataProduct

    @Multipart
    @POST("product")
    suspend fun tambahProductMultipart(
        @Part("nama") nama: RequestBody,
        @Part("price") price: RequestBody,
        @Part("desc") desc: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part image: MultipartBody.Part
    ): retrofit2.Response<Void>
    @Multipart

    @PUT("product/{id}")
    suspend fun updateProductMultipart(
        @Path("id") id: Int,
        @Part("nama") nama: RequestBody,
        @Part("price") price: RequestBody,
        @Part("desc") desc: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part image: MultipartBody.Part? // Dibuat opsional jika gambar tidak diganti
    ): retrofit2.Response<Void>


    @PUT("product/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body dataProduct: DataProduct):retrofit2.Response<Void>

    @DELETE("product/{id}")
    suspend fun hapusProduct(@Path("id") id: Int): retrofit2.Response<Void>

//    pesanan
    @GET("pesanan")
    suspend fun getAllPesanan(): List<DataPesanan>

    @GET("pesanan/{id}")
    suspend fun getPesananById(@Query("id") id: Int): DataPesanan

    @POST("pesanan")
    suspend fun tambahPesanan(@Body dataPesanan: DataPesanan):retrofit2.Response<Void>

    @PUT("pesanan/{id}")
    suspend fun updatePesanan(@Path("id") id: Int, @Body dataPesanan: DataPesanan):retrofit2.Response<Void>

    @DELETE("pesanan/{id}")
    suspend fun hapusPesanan(@Path("id") id: Int):retrofit2.Response<Void>

    @PUT("pesanan/{id}/nama")
    suspend fun updateNamaCust(@Query("id") id: Int, @Body namaCust: Map<String, String>):retrofit2.Response<Void>

    @GET("print/{id}")
    suspend fun printPesanan(@Path("id") id: Int):retrofit2.Response<ResponseBody>

    //    pesanan item
    @GET("pesanan-item")
    suspend fun getAllItemPesanan(): List<DataPesananItem>

    @GET("pesanan-item/{id}")
    suspend fun getItemPesananById(@Query("id") id: Int): DataPesananItem

    @POST("pesanan-item")
    suspend fun tambahItemPesanan(@Body dataPesananItem: DataPesananItem):retrofit2.Response<Void>

    @PUT("pesanan-item/{id}")
    suspend fun updateItemPesanan(@Path("id") id: Int, @Body data: Map<String, Int>): retrofit2.Response<Void>

    @DELETE("pesanan-item/{id}")
    suspend fun hapusItemPesanan(@Path("id") id: Int):retrofit2.Response<Void>
}