package com.example.finalproject_209.viewmodel.provider

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.finalproject_209.repository.WhimsyWhisk
import com.example.finalproject_209.viewmodel.HomeVM
import com.example.finalproject_209.viewmodel.pesanan.DaftarPesananVM
import com.example.finalproject_209.viewmodel.pesanan_item.DaftarPesananItemVM
import com.example.finalproject_209.viewmodel.product.DaftarProductVM
import com.example.finalproject_209.viewmodel.product.DetailProductVM
import com.example.finalproject_209.viewmodel.product.EditProductVM
import com.example.finalproject_209.viewmodel.product.EntryProductVM
import com.example.finalproject_209.viewmodel.user.LoginVM
import com.example.finalproject_209.viewmodel.user.ProfileVM

fun CreationExtras.whimsyWhisk(): WhimsyWhisk = (
        this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as
                WhimsyWhisk
        )
object PenyediaViewModel{
    val Factory = viewModelFactory {
        initializer { LoginVM(whimsyWhisk().container
            .repositoryDataUser) }
        initializer { HomeVM(
            whimsyWhisk().container.repositoryDataProduct,
            whimsyWhisk().container.repositoryDataItemPesanan,
            whimsyWhisk().container.repositoryDataPesanan) }
        //product
        initializer {
            DaftarProductVM(whimsyWhisk().container
                .repositoryDataProduct)
        }
        initializer {
            EntryProductVM(whimsyWhisk().container
                .repositoryDataProduct)
        }
        initializer {
            DetailProductVM( this.createSavedStateHandle(),
                whimsyWhisk().container.repositoryDataProduct)
        }
        initializer {
            EditProductVM(this.createSavedStateHandle(),
                whimsyWhisk().container.repositoryDataProduct)
        }
//        pesanan
        initializer {
            DaftarPesananVM(
                whimsyWhisk().container.repositoryDataPesanan,
                whimsyWhisk().container.repositoryDataUser,
                whimsyWhisk().container.repositoryDataItemPesanan,
                whimsyWhisk().container.repositoryDataProduct
            )
        }
        initializer {
            DaftarPesananItemVM(
                whimsyWhisk().container.repositoryDataItemPesanan,
                whimsyWhisk().container.repositoryDataProduct,
                whimsyWhisk().container.repositoryDataPesanan)

        }
        initializer {
            ProfileVM(whimsyWhisk().container.repositoryDataUser)
        }
    }
}