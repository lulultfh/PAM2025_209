package com.example.finalproject_209.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject_209.model.DataPesanan
import com.example.finalproject_209.model.DataPesananItem
import com.example.finalproject_209.model.DataProduct
import com.example.finalproject_209.model.Kategori
import com.example.finalproject_209.model.Status
import com.example.finalproject_209.repository.RepositoryDataItemPesanan
import com.example.finalproject_209.repository.RepositoryDataPesanan
import com.example.finalproject_209.repository.RepositoryDataProduct
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class HomeUiState {
    data class Success(val products: List<DataProduct>) : HomeUiState()
    object Loading : HomeUiState()
    object Error : HomeUiState()
}
sealed class HomeEvent {
    object AddedToCart : HomeEvent()
    object OutOfStock : HomeEvent()
    data class Error(val message: String) : HomeEvent()
}
class HomeVM(
    private val repositoryDataProduct: RepositoryDataProduct,
    private val repositoryDataItemPesanan: RepositoryDataItemPesanan,
    private val repositoryDataPesanan: RepositoryDataPesanan
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _selectedKategori = MutableStateFlow<Kategori?>(null)
    val selectedKategori = _selectedKategori.asStateFlow()

    private val _dataSource = MutableStateFlow<List<DataProduct>>(emptyList())
    private val _event = MutableSharedFlow<HomeEvent>()
    val event = _event.asSharedFlow()
    val products = combine(
        searchText.debounce(400),
        _dataSource,
        _selectedKategori
    ) { text, datas, kategori ->
        var result = datas

        if (kategori != null) {
            result = result.filter { it.kategori == kategori }
        }

        if (text.isNotBlank()) {
            result = result.filter {
                it.doesMatchSearchQuery(text)
            }
        }

        result
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            homeUiState = HomeUiState.Loading
            homeUiState = try {
                val products = repositoryDataProduct.getDataProduct()
                _dataSource.value = products
                HomeUiState.Success(products)
            } catch (e: Exception) {
                HomeUiState.Error
            }
        }
    }
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }
    fun onKategoriSelected(kategori: Kategori?) {
        _selectedKategori.value = kategori
    }
//    fun addToPesananItem(product: DataProduct) {
//        viewModelScope.launch {
//            try {
//                if (product.stok > 0) {
//                    val updatedProduct = product.copy(stok = product.stok - 1)
//                    repositoryDataProduct.editProduct(product.id, updatedProduct)
//                    val cartItems = repositoryDataItemPesanan.getAllItemPesanan()
//                    val existingItem = cartItems.find {
//                        it.product_id == product.id && it.pesanan_id == 0
//                    }
//                    if (existingItem != null) {
//                        val newQty = existingItem.qty + 1
//                        repositoryDataItemPesanan.editItemPesanan(
//                            id = existingItem.id,
//                            qty = newQty,
//                            subtotal = newQty * product.price
//                        )
//                    } else {
//                        repositoryDataItemPesanan.tambahItemPesanan(
//                            DataPesananItem(
//                                id = 0,
//                                pesanan_id = 0, // 0 menandakan belum di-checkout menjadi transaksi
//                                product_id = product.id,
//                                qty = 1,
//                                subtotal = product.price
//                            )
//                        )
//                    }
//                    _event.emit(HomeEvent.AddedToCart)
//                    loadProducts()
//                } else {
//                    _event.emit(HomeEvent.OutOfStock)
//                }
//            } catch (e: Exception) {
//                _event.emit(HomeEvent.Error("Gagal masuk keranjang: ${e.message}"))
//            }
//        }
//    }
fun addToPesananItem(product: DataProduct) {
    viewModelScope.launch {
        try {
            if (product.stok > 0) {
                // Langsung kurangi stok di database produk
                repositoryDataProduct.editProduct(product.id, product.copy(stok = product.stok - 1))

                // Kirim HANYA info produk, biarkan Backend cari pesanan_id nya
                repositoryDataItemPesanan.tambahItemPesanan(
                    DataPesananItem(
                        id = 0,
                        pesanan_id = 0, // Abaikan saja, Backend akan menimpa ini
                        product_id = product.id,
                        qty = 1,
                        subtotal = product.price
                    )
                )
                _event.emit(HomeEvent.AddedToCart)
                loadProducts()
            }
        } catch (e: Exception) {
            _event.emit(HomeEvent.Error("Gagal: ${e.message}"))
        }
    }
}
}
