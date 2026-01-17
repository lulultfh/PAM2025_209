package com.example.finalproject_209.ui.view.route.product

import com.example.finalproject_209.R
import com.example.finalproject_209.ui.view.route.DestinasiNavigasi

object DestinasiEditProduct : DestinasiNavigasi {
    override val route = "product_edit"
    override val titleRes = R.string.prod_edit
    const val itemIdArg = "idProduct"
    val routeWithArgs = "$route/{$itemIdArg}"
}