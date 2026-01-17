package com.example.finalproject_209.viewmodel.product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject_209.model.DataProduct
import com.example.finalproject_209.model.Kategori
import com.example.finalproject_209.model.ValidasiProductField
import com.example.finalproject_209.repository.RepositoryDataProduct
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed class DaftarProductUiState{
    data class Success(val productList: List<DataProduct>): DaftarProductUiState()
    object Error: DaftarProductUiState()
    object Loading: DaftarProductUiState()
}

class DaftarProductVM(private val repositoryDataProduct: RepositoryDataProduct)
    : ViewModel()
{
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching= _isSearching.asStateFlow()
    private val _selectedKategori = MutableStateFlow<Kategori?>(null)
    val selectedKategori = _selectedKategori.asStateFlow()
    private val _dataSearch = MutableStateFlow<List<DataProduct>>(emptyList())

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    private val _isErrorMessage = MutableStateFlow(false)
    val isErrorMessage = _isErrorMessage.asStateFlow()

    fun dismissMessage() {
        _message.value = null
    }
    private fun showValidationMessage(validation: ValidasiProductField) {
        val firstError = listOf(
            validation.errorNama,
            validation.errorPrice,
            validation.errorDesc,
            validation.errorStok,
            validation.errorKategori,
            validation.errorImage
        ).firstOrNull { it != null }

        if (firstError != null) {
            _message.value = firstError
            _isErrorMessage.value = true
        }
    }
    val products = combine(
        _searchText, // Langsung gunakan flow tanpa debounce di awal agar data tidak kosong saat start
        _dataSearch,
        _selectedKategori
    ) { text, datas, kategori ->
        var filtered = datas
        if (kategori != null) {
            filtered = filtered.filter { it.kategori == kategori }
        }
        if (text.isNotBlank()) {
            filtered = filtered.filter { it.doesMatchSearchQuery(text) }
        }
        filtered
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    var productUiState: DaftarProductUiState by mutableStateOf(DaftarProductUiState.Loading)
        private set
    init {
        getProduct()
    }
    fun getProduct(){
        viewModelScope.launch {
            productUiState = DaftarProductUiState.Loading
            productUiState = try {
                val productList = repositoryDataProduct.getDataProduct()
                _dataSearch.value = productList //buat memperbarui data utama
                DaftarProductUiState.Success(productList)
            }
            catch (e: IOException){
                DaftarProductUiState.Error
            }
            catch (e: HttpException){
                DaftarProductUiState.Error
            }
        }
    }
    fun onSearchTextChange(text: String) { _searchText.value = text }
    fun onKategoriSelected(kategori: Kategori?) {
        _selectedKategori.value = kategori
    }
    fun deleteProduct(product: DataProduct) {
        viewModelScope.launch {
            try {
                val response = repositoryDataProduct.hapusProduct(product.id)
                if (response.isSuccessful) {
                    println("BAKERY_DEBUG: Berhasil menghapus produk ${product.nama}")
                    // Refresh data setelah hapus agar list di UI terupdate
                    getProduct()
                } else {
                    println("BAKERY_DEBUG: Gagal menghapus. Kode: ${response.code()}")
                }
            } catch (e: Exception) {
                println("BAKERY_DEBUG: Error hapus -> ${e.message}")
            }
        }
    }
}