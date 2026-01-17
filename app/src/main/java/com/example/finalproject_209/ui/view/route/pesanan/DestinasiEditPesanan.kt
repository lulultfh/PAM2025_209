package com.example.finalproject_209.ui.view.route.pesanan

import com.example.finalproject_209.R
import com.example.finalproject_209.ui.view.route.DestinasiNavigasi

object DestinasiEditPesanan : DestinasiNavigasi {
    override val route = "pesanan_edit"
    override val titleRes = R.string.pesanan_edit
    const val itemIdArg = "idPesanan"
    val routeWithArgs = "$route/{$itemIdArg}"
}