package com.example.finalproject_209.ui.view.route.product

import com.example.finalproject_209.R
import com.example.finalproject_209.ui.view.route.DestinasiNavigasi

object DestinasiDetailProduct : DestinasiNavigasi {
    override val route = "detail_siswa"
    override val titleRes = R.string.prod_detail
    const val itemIdArg = "idProduct"
    val routeWithArgs = "$route/{$itemIdArg}"
}