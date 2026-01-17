package com.example.finalproject_209.viewmodel.product

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.finalproject_209.model.DetailProduct
import com.example.finalproject_209.model.UiStateProduct
import com.example.finalproject_209.model.ValidasiProductField
import com.example.finalproject_209.model.toDataProduct
import com.example.finalproject_209.model.validasiInputPerField
import com.example.finalproject_209.repository.RepositoryDataProduct
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import kotlin.String

class EntryProductVM(private val repositoryDataProduct: RepositoryDataProduct) : ViewModel() {
    var uiStateProduct by mutableStateOf(UiStateProduct())
        private set

    fun updateUiStateProduct(detailProduct: DetailProduct) {
        uiStateProduct = uiStateProduct.copy(
            detailProduct = detailProduct,
            validation = ValidasiProductField()
        )
    }

    // Fungsi untuk membuat nama file unik agar tidak bentrok di server
    private fun generateUniqueFileName(uriString: String): String {
        val uri = Uri.parse(uriString)
        // Ambil nama file asli atau gunakan 'img' sebagai default
        val originalName = uri.lastPathSegment?.substringAfterLast('/') ?: "img"
        val timestamp = System.currentTimeMillis()
        return "bakery_${timestamp}_$originalName.jpg"
    }
    suspend fun addProduct(context: Context): Boolean { // Tambahkan parameter context
        val product = uiStateProduct.detailProduct
        val validasiField = validasiInputPerField(product)

        if (!validasiField.isValid) {
            uiStateProduct = uiStateProduct.copy(isEntryValid = false, validation = validasiField)
            return false
        }
        return try {
            // 1. Siapkan data teks (Gunakan "text/plain")
            val namaBody = product.nama.toRequestBody("text/plain".toMediaType())
            val priceBody = product.price.toString().toRequestBody("text/plain".toMediaType())
            val descBody = product.desc.toRequestBody("text/plain".toMediaType())
            val stokBody = product.stok.toString().toRequestBody("text/plain".toMediaType())
            val kategoriBody = product.kategori.name.toRequestBody("text/plain".toMediaType())

            // 2. PROSES GAMBAR DENGAN MIMETYPE SPESIFIK
            val uri = Uri.parse(product.image)
            val contentResolver = context.contentResolver

            // Ambil MimeType asli dari URI (misal: image/jpeg atau image/png)
            val type = contentResolver.getType(uri) ?: "image/jpeg"
            val inputStream = contentResolver.openInputStream(uri)
            val fileBytes = inputStream?.readBytes() ?: return false

            // Buat RequestBody dengan MimeType yang dikenali server
            val requestFile = fileBytes.toRequestBody(type.toMediaTypeOrNull())

            // Buat MultipartBody.Part dengan nama file asli agar ekstensi terbaca server
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(type) ?: "jpg"
            val fileName = "product_${System.currentTimeMillis()}.$extension"

            val imagePart = MultipartBody.Part.createFormData("image", fileName, requestFile)

            // 3. Kirim ke Server
            val response = repositoryDataProduct.tambahProductMultipart(
                namaBody, priceBody, descBody, stokBody, kategoriBody, imagePart
            )

            if (response.isSuccessful) {
                println("BAKERY_DEBUG: Simpan Berhasil!")
                uiStateProduct = UiStateProduct()
                true
            } else {
                println("BAKERY_DEBUG: Gagal -> ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            println("BAKERY_DEBUG: Exception -> ${e.message}")
            false
        }
    }
}