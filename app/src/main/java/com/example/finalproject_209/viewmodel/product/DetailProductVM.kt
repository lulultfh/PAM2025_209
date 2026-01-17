package com.example.finalproject_209.viewmodel.product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject_209.model.DataProduct
import com.example.finalproject_209.model.DetailProduct
import com.example.finalproject_209.repository.RepositoryDataProduct
import com.example.finalproject_209.ui.view.route.product.DestinasiDetailProduct
import com.example.finalproject_209.ui.view.route.product.DestinasiProduct
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed class DetailProductUiState{
    data class Success(val satuProduct: DataProduct?): DetailProductUiState()
    object Error: DetailProductUiState()
    object Loading: DetailProductUiState()
}

class DetailProductVM (savedStateHandle: SavedStateHandle, private val repositoryDataProduct: RepositoryDataProduct): ViewModel(){
    private val idProduct: Int = checkNotNull(savedStateHandle[DestinasiDetailProduct.itemIdArg])
    var detailProductUiState: DetailProductUiState by mutableStateOf(DetailProductUiState.Loading)
        private set
    init {
        getSatuProduct()
    }
    fun getSatuProduct(){
        viewModelScope.launch {
            detailProductUiState = DetailProductUiState.Loading
            detailProductUiState = try {
                DetailProductUiState.Success(satuProduct = repositoryDataProduct.getDetailProduct(idProduct))
            }
            catch (e: IOException){
                DetailProductUiState.Error
            }
            catch (e: HttpException){
                DetailProductUiState.Error
            }
        }
    }

    suspend fun hapusSatuSiswa(){
        try {
            repositoryDataProduct.hapusProduct(idProduct)
            println("Sukses hapus data: $idProduct")
        }
        catch (e: Exception){
            println("Gagal hapus data: ${e.message}")
        }
    }
}