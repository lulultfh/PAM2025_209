package com.example.finalproject_209.viewmodel.product

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject_209.model.DetailProduct
import com.example.finalproject_209.model.Kategori
import com.example.finalproject_209.model.UiStateProduct
import com.example.finalproject_209.model.ValidasiProductField
import com.example.finalproject_209.model.toDataProduct
import com.example.finalproject_209.model.toUiStateProduct
import com.example.finalproject_209.model.validasiInputPerField
import com.example.finalproject_209.repository.RepositoryDataProduct
import com.example.finalproject_209.ui.view.route.product.DestinasiDetailProduct
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

class EditProductVM(
    savedStateHandle: SavedStateHandle,
    private val repositoryDataProduct: RepositoryDataProduct
) : ViewModel() {
    var uiStateProduct by mutableStateOf(UiStateProduct())
        private set

    private val idProduct: Int = checkNotNull(savedStateHandle[DestinasiDetailProduct.itemIdArg])

    init {
        viewModelScope.launch {
            uiStateProduct = repositoryDataProduct.getDetailProduct(idProduct)
                .toUiStateProduct(true)
        }
    }

    fun updateUiState(detailProduct: DetailProduct) {
        uiStateProduct = uiStateProduct.copy(detailProduct = detailProduct, validation = ValidasiProductField())
    }

    suspend fun editSatuProduct(context: Context): Boolean {
        val product = uiStateProduct.detailProduct
        val validasiField = validasiInputPerField(product)

        if (!validasiField.isValid) {
            uiStateProduct = uiStateProduct.copy(isEntryValid = false, validation = validasiField)
            return false
        }

        return try {
            val namaBody = product.nama.toRequestBody("text/plain".toMediaType())
            val priceBody = product.price.toString().toRequestBody("text/plain".toMediaType())
            val descBody = product.desc.toRequestBody("text/plain".toMediaType())
            val stokBody = product.stok.toString().toRequestBody("text/plain".toMediaType())
            val kategoriBody = product.kategori.name.toRequestBody("text/plain".toMediaType())

            val imagePart = if (product.image.startsWith("content://")) {
                val uri = Uri.parse(product.image)
                val type = context.contentResolver.getType(uri) ?: "image/jpeg"
                val inputStream = context.contentResolver.openInputStream(uri)
                val fileBytes = inputStream?.readBytes()
                val requestFile = fileBytes?.toRequestBody(type.toMediaTypeOrNull())
                requestFile?.let { MultipartBody.Part.createFormData("image", "update.jpg", it) }
            } else null

            // Panggil repository fungsi multipart (kamu perlu menambahkannya di repo juga)
            val response = repositoryDataProduct.updateProductMultipart(
                idProduct, namaBody, priceBody, descBody, stokBody, kategoriBody, imagePart
            )
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}