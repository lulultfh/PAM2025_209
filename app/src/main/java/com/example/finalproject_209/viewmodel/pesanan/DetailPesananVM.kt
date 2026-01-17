package com.example.finalproject_209.viewmodel.pesanan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject_209.repository.RepositoryDataPesanan
import com.example.finalproject_209.repository.RepositoryDataUser
import com.example.finalproject_209.ui.view.route.pesanan.DestinasiDetailPesanan
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed class DetailPesananUiState{
    data class Success(val join: Join): DetailPesananUiState()
    object Error: DetailPesananUiState()
    object Loading: DetailPesananUiState()
}
class DetailPesananVM(savedStateHandle: SavedStateHandle,
    private val repositoryDataPesanan: RepositoryDataPesanan,
    private val repositoryDataUser: RepositoryDataUser
): ViewModel(){
    private val idPesanan: Int = checkNotNull(savedStateHandle[DestinasiDetailPesanan.itemIdArg])
    var detailPesananUiState: DetailPesananUiState by mutableStateOf(DetailPesananUiState.Loading)
        private set
    init {
        getSatuPesanan()
    }
    fun getSatuPesanan(){
        viewModelScope.launch {
            detailPesananUiState = DetailPesananUiState.Loading
            detailPesananUiState = try {
                val pesanan = repositoryDataPesanan.getPesananById(idPesanan)
                val user = repositoryDataUser.getUserById(pesanan.admin_id)
                val combined = Join(pesanan, user)
                DetailPesananUiState.Success(combined)
            } catch (e: IOException){
                DetailPesananUiState.Error
            }
            catch (e: HttpException){
                DetailPesananUiState.Error
            }
        }
    }
    fun deleteSatuPesanan(){
        viewModelScope.launch {
            try {
                val response = repositoryDataPesanan.hapusPesanan(idPesanan)
                if (response.isSuccessful){
                    println("Sukses hapus data: $idPesanan")
                }
                else{
                    println("Gagal hapus data: ${response.message()}")
                }
            } catch (e: Exception){
                println("Gagal hapus data: ${e.message}")
            }
        }
    }
}