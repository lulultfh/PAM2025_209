package com.example.finalproject_209.viewmodel.pesanan_item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.finalproject_209.model.DataPesanan
import com.example.finalproject_209.model.DataPesananItem
import com.example.finalproject_209.model.DataProduct
import com.example.finalproject_209.model.Status
import com.example.finalproject_209.repository.RepositoryDataItemPesanan
import com.example.finalproject_209.repository.RepositoryDataPesanan
import com.example.finalproject_209.repository.RepositoryDataProduct
import com.example.finalproject_209.viewmodel.pesanan.DaftarPesananUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed class DaftarItemPesananUiState{
    data class Success(val join: List<Join>): DaftarItemPesananUiState()
    object Error: DaftarItemPesananUiState()
    object Loading: DaftarItemPesananUiState()
}
data class Join(
    val pesananItem: DataPesananItem,
    val product: DataProduct?,
    val pesanan: DataPesanan?
)
class DaftarPesananItemVM(
    private val repositoryDataItemPesanan: RepositoryDataItemPesanan,
    private val repositoryDataProduct: RepositoryDataProduct,
    private val repositoryDataPesanan: RepositoryDataPesanan
): ViewModel(){
    var namaCustInput by mutableStateOf("")
    var listItemPesanan: DaftarItemPesananUiState by mutableStateOf(DaftarItemPesananUiState.Loading)
        private set
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _isErrorMessage = MutableStateFlow(false)
    val isErrorMessage: StateFlow<Boolean> = _isErrorMessage

    fun dismissMessage() {
        _message.value = null
    }
    private val _data = MutableStateFlow<List<Join>>(emptyList())
    init {
        LoadPesananItem()
    }
    fun LoadPesananItem(){
        viewModelScope.launch {
            listItemPesanan = DaftarItemPesananUiState.Loading
            listItemPesanan = try {
                val pesananItemList = repositoryDataItemPesanan.getAllItemPesanan()
                val productList = repositoryDataProduct.getDataProduct()
                val pesananList = repositoryDataPesanan.getAllPesanan()

                val combinedList = pesananItemList.mapNotNull { pesananItem ->
                    val pesanan = pesananList.find { it.id == pesananItem.pesanan_id }
                    if (pesanan?.status != Status.process) return@mapNotNull null

                    val product = productList.find { it.id == pesananItem.product_id }
                    Join(pesananItem, product, pesanan)
                }
                _data.value = combinedList
                DaftarItemPesananUiState.Success(combinedList)
            }catch (e: IOException){
                DaftarItemPesananUiState.Error
            }
            catch (e: HttpException){
                DaftarItemPesananUiState.Error
            }
        }
    }
    fun IncreaseQty(item: DataPesananItem, harga: Int) {
        viewModelScope.launch {
            try {
                val newQty = item.qty + 1
                val newSubTotal = newQty * harga

                repositoryDataItemPesanan.editItemPesanan(
                    id = item.id,
                    qty = newQty,
                    subtotal = newSubTotal
                )
                _message.value = "Jumlah produk ditambahkan"
                _isErrorMessage.value = false

                LoadPesananItem() // Refresh UI
            } catch (e: Exception) {
                _message.value = "Gagal menambah jumlah produk"
                _isErrorMessage.value = true
                listItemPesanan = DaftarItemPesananUiState.Error
            }
        }
    }
    fun DecreaseQty(item: DataPesananItem, harga: Int){ //ngurangin item
        viewModelScope.launch {
            try {
                if (item.qty > 1) {
                    val newQty = item.qty - 1
                    val newSubTotal = newQty * harga
                    repositoryDataItemPesanan.editItemPesanan(item.id, newQty, newSubTotal)

                    _message.value = "Jumlah produk dikurangi"
                    _isErrorMessage.value = false
                } else {
                    RemoveItem(item)

                    _message.value = "Produk dihapus dari keranjang"
                    _isErrorMessage.value = false
                }
                LoadPesananItem()
            } catch (e: Exception) {
                _message.value = "Gagal mengurangi produk"
                _isErrorMessage.value = true
                println("Error Decrease: ${e.message}")
            }
        }
    }
    fun RemoveItem(item: DataPesananItem){
        viewModelScope.launch {
            val updated = _data.value.filter {
                it.pesananItem.id != item.id
            }
            _data.value = updated
            listItemPesanan = DaftarItemPesananUiState.Success(updated)
        }
    }
    fun updateCustName(pesananId: Int, namaCust: String) {
        namaCustInput = namaCust // Update state lokal dulu supaya ngetik lancar
        viewModelScope.launch {
            try {
                repositoryDataPesanan.updateNamaCust(id = pesananId, namaCust = namaCust)
            } catch (e: Exception) {
                println("Gagal update nama: ${e.message}")
            }
        }
    }
    fun getTotal(items: List<Join>): Int{
        return items.sumOf { it.pesananItem.subtotal }
    }
    fun checkout() {
        viewModelScope.launch {
            try {
                val items = _data.value
                if (items.isEmpty()) {
                    _message.value = "Keranjang masih kosong"
                    _isErrorMessage.value = true
                } else {
                    val pesanan = items.first().pesanan
                    if (pesanan == null || pesanan.namaCust.isBlank()) {
                        _message.value = "Nama customer wajib diisi"
                        _isErrorMessage.value = true
                    } else {
                        val totalHarga = getTotal(items)
                        val namaFix =
                            if (namaCustInput.isNotBlank()) namaCustInput else pesanan.namaCust

                        val dataPesananBaru = pesanan.copy(
                            namaCust = namaFix,
                            total_harga = totalHarga,
                            status = Status.finish
                        )

                        repositoryDataPesanan.editPesanan(pesanan.id, dataPesananBaru)

                        _message.value = "Checkout berhasil"
                        _isErrorMessage.value = false

                        LoadPesananItem()
                    }
                }
            } catch (e: Exception) {
                _message.value = "Checkout gagal"
                _isErrorMessage.value = true
                println("Gagal checkout: ${e.message}")
            }
        }
    }

}