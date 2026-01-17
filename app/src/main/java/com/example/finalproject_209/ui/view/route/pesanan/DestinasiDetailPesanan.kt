package com.example.finalproject_209.ui.view.route.pesanan

import com.example.finalproject_209.R
import com.example.finalproject_209.ui.view.route.DestinasiNavigasi

object DestinasiDetailPesanan : DestinasiNavigasi {
    override val route = "pesanan_detail"
    override val titleRes = R.string.pesanan_detail
    const val itemIdArg = "idPesanan"
    val routeWithArgs = "$route/{$itemIdArg}"
}