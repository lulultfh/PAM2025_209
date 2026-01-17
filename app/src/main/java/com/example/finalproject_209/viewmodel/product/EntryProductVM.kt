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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import kotlin.String

class EntryProductVM(private val repositoryDataProduct: RepositoryDataProduct) : ViewModel() {
    var uiStateProduct by mutableStateOf(UiStateProduct())
        private set
    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    private val _isErrorMessage = MutableStateFlow(false)
    val isErrorMessage = _isErrorMessage.asStateFlow()

    fun dismissMessage() {
        _message.value = null
    }
    fun updateUiStateProduct(detailProduct: DetailProduct) {
        uiStateProduct = uiStateProduct.copy(
            detailProduct = detailProduct,
            validation = ValidasiProductField()
        )
    }
    suspend fun addProduct(context: Context): Boolean { // Tambahkan parameter context
        val product = uiStateProduct.detailProduct
        val validasiField = validasiInputPerField(product)

        if (!validasiField.isValid) {
            uiStateProduct = uiStateProduct.copy(
                isEntryValid = false,
                validation = validasiField
            )
            _message.value = "Data produk belum valid"
            _isErrorMessage.value = true
            return false
        }
        return try {
            val namaBody = product.nama.toRequestBody("text/plain".toMediaType())
            val priceBody = product.price.toString().toRequestBody("text/plain".toMediaType())
            val descBody = product.desc.toRequestBody("text/plain".toMediaType())
            val stokBody = product.stok.toString().toRequestBody("text/plain".toMediaType())
            val kategoriBody = product.kategori.name.toRequestBody("text/plain".toMediaType())
            val uri = Uri.parse(product.image)
            val contentResolver = context.contentResolver
            val type = contentResolver.getType(uri) ?: "image/jpeg"
            val inputStream = contentResolver.openInputStream(uri)
            val fileBytes = inputStream?.readBytes() ?: return false
            val requestFile = fileBytes.toRequestBody(type.toMediaTypeOrNull())
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(type) ?: "jpg"
            val fileName = "product_${System.currentTimeMillis()}.$extension"
            val imagePart = MultipartBody.Part.createFormData("image", fileName, requestFile)
            val response = repositoryDataProduct.tambahProductMultipart(
                namaBody, priceBody, descBody, stokBody, kategoriBody, imagePart
            )

            if (response.isSuccessful) {
                uiStateProduct = UiStateProduct()

                _message.value = "Produk berhasil ditambahkan"
                _isErrorMessage.value = false
                true
            } else {
                _message.value = "Gagal menyimpan produk"
                _isErrorMessage.value = true
                false
            }
        } catch (e: Exception) {
            _message.value = "Terjadi kesalahan server"
            _isErrorMessage.value = true
            false
        }
    }
}