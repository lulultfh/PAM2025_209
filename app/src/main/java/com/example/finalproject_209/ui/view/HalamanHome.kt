package com.example.finalproject_209.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.finalproject_209.R
import com.example.finalproject_209.model.DataProduct
import com.example.finalproject_209.model.DataUser
import com.example.finalproject_209.model.Kategori
import com.example.finalproject_209.model.UiStateProduct
import com.example.finalproject_209.repository.BakeryContainer
import com.example.finalproject_209.repository.WhimsyWhisk
import com.example.finalproject_209.ui.customwidget.ProductQuickviewDialog
import com.example.finalproject_209.ui.view.customwidget.BakeryTopBar
import com.example.finalproject_209.ui.view.customwidget.BottomBarNav
import com.example.finalproject_209.ui.view.route.DestinasiHome
import com.example.finalproject_209.viewmodel.HomeEvent
import com.example.finalproject_209.viewmodel.HomeUiState
import com.example.finalproject_209.viewmodel.HomeVM
import com.example.finalproject_209.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToCart:() -> Unit,
    navigateToUser:() -> Unit,
    navigateToHome: () -> Unit,
    navigateToProduct:() -> Unit,
    navigateToTrans:() -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeVM = viewModel(factory = PenyediaViewModel.Factory)
){
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val searchText by viewModel.searchText.collectAsState()
    val selectedKategori by viewModel.selectedKategori.collectAsState()
    val products by viewModel.products.collectAsState()
    var showQuickviewByProduct by remember { mutableStateOf<DataProduct?>(null) }
    val uiState = viewModel.homeUiState
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is HomeEvent.AddedToCart -> {
                    navigateToCart()  // Berhasil tambah, langsung pindah ke halaman keranjang
                }
                is HomeEvent.OutOfStock -> {
                    android.widget.Toast.makeText(context, "Stok habis!", android.widget.Toast.LENGTH_SHORT).show()
                }
                is HomeEvent.Error -> {
                    android.widget.Toast.makeText(context, event.message, android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    Scaffold(
        topBar = {
            BakeryTopBar(
                title = stringResource(DestinasiHome.titleRes),
                isHome = true,
                canNavigateBack = false,
                searchText = searchText,
                onSearchChange = viewModel::onSearchTextChange,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToCart,
                containerColor = colorResource(R.color.pink2),
                contentColor = Color.White,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.shopping_cart),
                    contentDescription = "add-to-cart",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        bottomBar = {
            BottomBarNav(
                selectedTab = 0,
                navigateToHome = navigateToHome,
                navigateToProduct = navigateToProduct,
                navigateToTransaksi = navigateToTrans,
                navigateToUser = navigateToUser
            )
        }
    ) { innerPadding ->
        HomeBody(
            statusUiProduct = uiState,
            products = products,
            onProductClick = { product ->
                showQuickviewByProduct = product
            },
            onAddToCart = {product ->
                viewModel.addToPesananItem(product)
            },
            retryAction = {},
            selectedKategori = selectedKategori,
            onSelectedKategori = viewModel::onKategoriSelected,
            modifier = Modifier
                .background(colorResource(R.color.pink1))
                .padding(innerPadding)
        )
        showQuickviewByProduct?.let { product ->
            ProductQuickviewDialog(
                product = product,
                onDismiss = { showQuickviewByProduct = null }
            )
        }
    }
}
@Composable
fun HomeBody(
    statusUiProduct: HomeUiState,
    products: List<DataProduct>,
    onProductClick: (DataProduct) -> Unit,
    onAddToCart: (DataProduct) -> Unit,
    retryAction: () -> Unit,
    selectedKategori: Kategori?,
    onSelectedKategori: (Kategori?) -> Unit,
    modifier: Modifier = Modifier
){
    when (statusUiProduct) {
        is HomeUiState.Loading -> LoadingScreen()
        is HomeUiState.Error -> ErrorScreen(retryAction)
        is HomeUiState.Success -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
            ) {
                FilterKategori(
                    selectedKategori = selectedKategori,
                    onSelectedKategori = onSelectedKategori
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (products.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Produk tidak ditemukan",
                            color = colorResource(R.color.pink2),
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    ProductList(
                        listProduct = products,
                        onProductClick = onProductClick,
                        onAddToCart = onAddToCart,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
@Composable
fun ProductList(
    listProduct: List<DataProduct>,
    onProductClick: (DataProduct) -> Unit,
    onAddToCart: (DataProduct) -> Unit,
    modifier: Modifier = Modifier
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = listProduct, key = {it.id}){
            product ->
            ProductCard(
                product = product,
                onAddClick = { onAddToCart(product) },
                modifier = Modifier
                    .clickable { onProductClick(product)}
            )
        }
    }
}

@Composable
fun ProductCard(
    product: DataProduct,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Card(modifier = modifier.width(160.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.hijau1)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            val imageUrl = imageBaseUrl()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ){
                AsyncImage(
                    model = imageUrl + product.image,
                    contentDescription = product.nama,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = product.nama,
                        color = colorResource(R.color.pink2),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text =" Rp ${ product.price }",
                        color = colorResource(R.color.pink2),
                        fontWeight = FontWeight.Light,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    onClick = onAddClick,
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .size(32.dp)
                        .background(colorResource(R.color.pink2), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(retryAction:() -> Unit,
                modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.gagal), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
){
    AsyncImage(
        modifier = modifier.size(100.dp),
        model = R.drawable.load,
        contentDescription = stringResource(R.string.load)
    )
}

@Composable
fun FilterKategori(
    selectedKategori: Kategori?,
    onSelectedKategori: (Kategori?) -> Unit
){
    LazyRow(
        modifier = Modifier.padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedKategori == null,
                onClick = {onSelectedKategori(null)},
                label = {Text("All",
                    color = colorResource(R.color.pink2))},
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.White,
                    selectedContainerColor = colorResource(R.color.hijau1) // Hijau1 kalau dipilih
                )
            )
        }
        items(Kategori.values()){ kategori ->
            FilterChip(
                selected = selectedKategori == kategori,
                onClick = {onSelectedKategori(kategori)},
                label = {Text(kategori.name,
                    color = colorResource(R.color.pink2))},
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.White,
                    selectedContainerColor = colorResource(R.color.hijau1)
                )
            )
        }
    }
}
@Composable
fun imageBaseUrl(): String {
    val context = LocalContext.current.applicationContext as WhimsyWhisk
    return (context.container as BakeryContainer).imageUrl
}