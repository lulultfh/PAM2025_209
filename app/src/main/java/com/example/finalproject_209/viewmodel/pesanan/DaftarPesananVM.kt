package com.example.finalproject_209.viewmodel.pesanan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject_209.model.DataPesanan
import com.example.finalproject_209.model.DataPesananItem
import com.example.finalproject_209.model.DataProduct
import com.example.finalproject_209.model.DataUser
import com.example.finalproject_209.model.Status
import com.example.finalproject_209.repository.RepositoryDataItemPesanan
import com.example.finalproject_209.repository.RepositoryDataPesanan
import com.example.finalproject_209.repository.RepositoryDataProduct
import com.example.finalproject_209.repository.RepositoryDataUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed class DaftarPesananUiState{
    data class Success(val join: List<Join>): DaftarPesananUiState()
    object Error: DaftarPesananUiState()
    object Loading: DaftarPesananUiState()
}
data class Join(
    val pesanan: DataPesanan,
    val user: DataUser?
)
data class ItemDetailJoin(
    val detail: DataPesananItem,
    val namaProduk: String,
    val hargaSatuan: Int
)
class DaftarPesananVM(private val repositoryDataPesanan: RepositoryDataPesanan,
    private val repositoryDataUser: RepositoryDataUser,
    private val repositoryDataItemPesanan: RepositoryDataItemPesanan,
    private val repositoryDataProduct: RepositoryDataProduct): ViewModel(){
    var listPesanan: DaftarPesananUiState by mutableStateOf(DaftarPesananUiState.Loading)
        private set
    private var currentFilter: Status? = null
    private val _dataSearch = MutableStateFlow<List<Join>>(emptyList())
    var listDetailItem by mutableStateOf<List<ItemDetailJoin>>(emptyList())
        private set
    init {
        LoadPesanan()
    }
    fun loadDetailItem(pesananId: Int) {
        viewModelScope.launch {
            try {
                val allProducts = repositoryDataProduct.getDataProduct()
                val allItems = repositoryDataItemPesanan.getAllItemPesanan()
                listDetailItem = allItems.filter { it.pesanan_id == pesananId }.map { item ->
                    val produk = allProducts.find { it.id == item.product_id }
                    ItemDetailJoin(
                        detail = item,
                        namaProduk = produk?.nama ?: "Produk Hilang",
                        hargaSatuan = produk?.price ?: 0
                    )
                }
            } catch (e: Exception) {
                listDetailItem = emptyList()
            }
        }
    }
    fun LoadPesanan(){
        viewModelScope.launch {
            listPesanan = DaftarPesananUiState.Loading
            listPesanan = try {
                val pesananList = repositoryDataPesanan.getAllPesanan()
                val userList = repositoryDataUser.getUser() ?: emptyList()

                val combinedList = pesananList.map { pesanan ->
                    val user = userList.find { it.id == pesanan.admin_id }
                    Join(pesanan, user)
                }
                _dataSearch.value = combinedList
                DaftarPesananUiState.Success(combinedList)
            }catch (e: IOException){
                DaftarPesananUiState.Error
            }
            catch (e: HttpException){
                DaftarPesananUiState.Error
            }
        }
    }
    fun updateStatus(pesanan: DataPesanan, status: Status) {
        viewModelScope.launch {
            try {
                // copy() menjamin total_harga yang lama tetap terbawa, tidak ter-reset
                val updatedPesanan = pesanan.copy(status = status)

                repositoryDataPesanan.editPesanan(
                    id = pesanan.id,
                    dataPesanan = updatedPesanan
                )
                LoadPesanan() // Refresh UI agar badge berubah hijau
            } catch (e: Exception) {
                println("Gagal update status: ${e.message}")
            }
        }
    }
    fun hapusPesanan(id: Int){
        viewModelScope.launch { repositoryDataPesanan.hapusPesanan(id)
        LoadPesanan()}
    }
    fun filterByStatus(status: Status) {
        viewModelScope.launch {
            val allData = _dataSearch.value
            currentFilter =
                if (currentFilter == status) null else status
            val filtered = currentFilter?.let { st ->
                allData.filter { it.pesanan.status == st }
            } ?: allData
            listPesanan = DaftarPesananUiState.Success(filtered)
        }
    }
}