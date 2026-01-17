package com.example.finalproject_209.ui.view.uicontroller

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.finalproject_209.ui.view.HomeScreen
import com.example.finalproject_209.ui.view.Login
import com.example.finalproject_209.ui.view.Product.EditProductScreen
import com.example.finalproject_209.ui.view.Product.EntryProductScreen
import com.example.finalproject_209.ui.view.Product.ProductScreen
import com.example.finalproject_209.ui.view.pesanan.PesananScreen
import com.example.finalproject_209.ui.view.pesanan_item.DaftarPesananItemScreen
import com.example.finalproject_209.ui.view.profile.ProfileScreen
import com.example.finalproject_209.ui.view.route.DestinasiHome
import com.example.finalproject_209.ui.view.route.DestinasiProfile
import com.example.finalproject_209.ui.view.route.autentikasi.DestinasiLogin
import com.example.finalproject_209.ui.view.route.pesanan.DestinasiDaftarPesanan
import com.example.finalproject_209.ui.view.route.pesanan_item.DestinasiItemPesanan
import com.example.finalproject_209.ui.view.route.product.DestinasiEditProduct
import com.example.finalproject_209.ui.view.route.product.DestinasiEntryProduct
import com.example.finalproject_209.ui.view.route.product.DestinasiProduct

//@Composable
//fun DataBakeryApp(
//    navController: NavHostController = rememberNavController(),
//    modifier: Modifier
//){
//    HostNavigasi(
//        navController = navController,
//        modifier = modifier)
//}
@Composable
fun DataBakeryApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
){
    HostNavigasi(
        navController = navController,
        modifier = modifier
    )
}
sealed class Screen(val route: String){
    object Home: Screen ("home")
}
@Composable
fun HostNavigasi(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = DestinasiLogin.route,
        modifier = modifier){
        composable(DestinasiLogin.route) {
            Login(
//                navigateToItemEntry = { navController.navigate(DestinasiEntry.route) },
//                navigateToItemDetail = { id -> navController.navigate("${DestinasiDetail.route}/$id") }
                onSubmit = {navController.navigate((DestinasiHome.route))}
            )
        }
        composable(DestinasiHome.route) {
            HomeScreen(
                navigateToUser = {navController.navigate((DestinasiProfile.route))},
                navigateToHome = {navController.navigate((DestinasiHome.route))},
                navigateToProduct = {navController.navigate((DestinasiProduct.route))},
                navigateToTrans = {navController.navigate((DestinasiDaftarPesanan.route))},
                navigateToCart = {navController.navigate((DestinasiItemPesanan.route))}
            )
        }
        composable(DestinasiProduct.route) {
            ProductScreen(navigateToEdit = {id -> navController.navigate("${ DestinasiEditProduct.route }/$id")},
                navigateToItemEntry = {navController.navigate((DestinasiEntryProduct.route))},
                navigateToUser = {navController.navigate((DestinasiProfile.route))},
                navigateToHome = {navController.navigate((DestinasiHome.route))},
                navigateToTrans = {navController.navigate((DestinasiDaftarPesanan.route))},
                navigateToProduct = {navController.navigate((DestinasiProduct.route))}
            )
        }
        composable(DestinasiEntryProduct.route) {
            EntryProductScreen(
                navigateBack = {navController.popBackStack()}
            )
        }
        composable(
            route = "${DestinasiEditProduct.route}/{${DestinasiEditProduct.itemIdArg}}",
            arguments = listOf(
                navArgument(DestinasiEditProduct.itemIdArg) {
                    type = NavType.IntType
                }
            )
        ) {
            EditProductScreen(
                navigateBack = {navController.popBackStack()}
            )
        }
        composable(DestinasiItemPesanan.route) {
            DaftarPesananItemScreen(
                navigateBack = {navController.popBackStack()},
                onPaymentSuccess = {navController.navigate((DestinasiDaftarPesanan.route))}
            )
        }
        composable(DestinasiDaftarPesanan.route) {
            PesananScreen(
                navigateToUser = {navController.navigate((DestinasiProfile.route))},
                navigateToHome = {navController.navigate((DestinasiHome.route))},
                navigateToTrans = {navController.navigate((DestinasiDaftarPesanan.route))},
                navigateToProduct = {navController.navigate((DestinasiProduct.route))},
            )
        }
        composable(DestinasiProfile.route) {
            ProfileScreen(
                navigateToHome = {navController.navigate((DestinasiHome.route))},
                navigateToTrans = {navController.navigate((DestinasiDaftarPesanan.route))},
                navigateToProduct = {navController.navigate((DestinasiProduct.route))},
                onLogoutClick = {navController.navigate(DestinasiLogin.route)}
            )
        }
    }
}