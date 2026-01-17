package com.example.finalproject_209.ui.view.customwidget

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.finalproject_209.R

//@Composable
//fun BottomBarNav(
//    selectedTab: Int,
//    navigateToHome:() -> Unit,
//    navigateToUser:() -> Unit,
//    navigateToProduct:() -> Unit,
//    navigateToTransaksi:() -> Unit
//){
//    Surface(
//        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
//        color = colorResource(R.color.pink2),
//    ) {
//        BottomAppBar(
//            containerColor = Color.Transparent
//        ) {
//            NavigationBarItem(
//                icon = {
//                    Icon(
//                        painter = painterResource(R.drawable.home1),
//                        contentDescription = null,
//                        modifier = Modifier.size(24.dp),
//                        tint = colorResource(R.color.pink1)
//                    )
//                },
//                label = { Text("Home", color = colorResource(R.color.pink1)) },
//                selected = selectedTab == 0,
//                onClick = {
//                    navigateToHome()
//                }
//            )
//            NavigationBarItem(
//                icon = {
//                    Icon(
//                        painter = painterResource(R.drawable.product),
//                        contentDescription = null,
//                        modifier = Modifier.size(24.dp),
//                        tint = colorResource(R.color.pink1)
//                    )
//                },
//                label = { Text("Product", color = colorResource(R.color.pink1)) },
//                selected = selectedTab == 1,
//                onClick = {
//                    navigateToProduct()
//                }
//            )
//            NavigationBarItem(
//                icon = {
//                    Icon(
//                        painter = painterResource(R.drawable.transaksi),
//                        contentDescription = null,
//                        modifier = Modifier.size(24.dp),
//                        tint = colorResource(R.color.pink1)
//                    )
//                },
//                label = { Text("Transaction", color = colorResource(R.color.pink1)) },
//                selected = selectedTab == 2,
//                onClick = {
//                    navigateToTransaksi()
//                }
//            )
//            NavigationBarItem(
//                icon = {
//                    Icon(
//                        painter = painterResource(R.drawable.profile),
//                        contentDescription = null,
//                        modifier = Modifier.size(24.dp),
//                        tint = colorResource(R.color.pink1)
//                    )
//                },
//                label = { Text("Profile", color = colorResource(R.color.pink1)) },
//                selected = selectedTab == 3,
//                onClick = {
//                    navigateToUser()
//                }
//            )
//        }
//    }
//}

@Composable
fun BottomBarNav(
    selectedTab: Int,
    navigateToHome: () -> Unit,
    navigateToUser: () -> Unit,
    navigateToProduct: () -> Unit,
    navigateToTransaksi: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        color = colorResource(R.color.pink2), // Warna background utama bar
        tonalElevation = 8.dp
    ) {
        BottomAppBar(
            modifier = Modifier.height(84.dp),
            containerColor = Color.Transparent,
            contentColor = colorResource(R.color.pink1),
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            val items = listOf(
                Triple("Home", R.drawable.home1, navigateToHome),
                Triple("Product", R.drawable.product, navigateToProduct),
                Triple("Transaction", R.drawable.transaksi, navigateToTransaksi),
                Triple("Profile", R.drawable.profile, navigateToUser)
            )
            items.forEachIndexed { index, item ->
                val isSelected = selectedTab == index
                NavigationBarItem(
                    selected = isSelected,
                    onClick = item.third,
                    icon = {
                        Icon(
                            painter = painterResource(item.second),
                            contentDescription = item.first,
                            modifier = Modifier.size(20.dp),
//                            tint = colorResource(R.color.pink1)
                        )
                    },
                    label = {
                        Text(
                            text = item.first,
                            color = colorResource(R.color.pink1)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = colorResource(R.color.hijau1),
                        unselectedIconColor = colorResource(R.color.pink1),
                        selectedIconColor = colorResource(R.color.pink2),
                        unselectedTextColor = colorResource(R.color.pink1),
                        selectedTextColor = colorResource(R.color.pink2)
                    )
                )
            }
        }
    }
}