package com.example.finalproject_209.ui.view.Product

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.finalproject_209.R
import com.example.finalproject_209.model.DataProduct
import com.example.finalproject_209.model.Kategori
import com.example.finalproject_209.repository.BakeryContainer
import com.example.finalproject_209.repository.WhimsyWhisk
import com.example.finalproject_209.ui.customwidget.ProductQuickviewDialog
import com.example.finalproject_209.ui.view.customwidget.BakeryMessageBar
import com.example.finalproject_209.ui.view.customwidget.BakeryTopBar
import com.example.finalproject_209.ui.view.customwidget.BottomBarNav
import com.example.finalproject_209.ui.view.route.product.DestinasiProduct
import com.example.finalproject_209.viewmodel.product.DaftarProductUiState
import com.example.finalproject_209.viewmodel.product.DaftarProductVM
import com.example.finalproject_209.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    navigateToEdit: (Int) -> Unit,
    navigateToUser:() -> Unit,
    navigateToHome: () -> Unit,
    navigateToProduct:() -> Unit,
    navigateToTrans:() -> Unit,
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DaftarProductVM = viewModel(factory = PenyediaViewModel.Factory)
) {
    LaunchedEffect(Unit) {
        viewModel.getProduct()
    }
    val uiState = viewModel.productUiState
    val filteredProducts by viewModel.products.collectAsState()
    val selectedKategori by viewModel.selectedKategori.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var showQuickviewByProduct by remember { mutableStateOf<DataProduct?>(null) }
    val message by viewModel.message.collectAsState()
    val isError by viewModel.isErrorMessage.collectAsState()

    Scaffold(
        topBar = {
            BakeryTopBar(
                title = stringResource(DestinasiProduct.titleRes),
                isHome = false,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                containerColor = colorResource(R.color.pink2),
                contentColor = Color.White,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add"
                )
            }
        },
        bottomBar = {
            BottomBarNav(
                selectedTab = 1,
                navigateToHome = navigateToHome,
                navigateToProduct = navigateToProduct,
                navigateToTransaksi = navigateToTrans,
                navigateToUser = navigateToUser
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.pink1))
        ){
            ProductBody(
                statusUiProductVM = uiState,
                products = filteredProducts, // Kirim data hasil filter
                selectedKategori = selectedKategori,
                onSelectedKategori = viewModel::onKategoriSelected,
                onProduct = { product ->
                    showQuickviewByProduct = product },
                onEdit = navigateToEdit,
                onDelete = { viewModel.deleteProduct(it) },
                retryAction = {},
                modifier = Modifier
                    .background(colorResource(R.color.pink1))
                    .padding(innerPadding)
                    .fillMaxSize()
            )
            message?.let {
                BakeryMessageBar(
                    message = it,
                    isError = isError,
                    onDismiss = { viewModel.dismissMessage() }
                )
            }
            showQuickviewByProduct?.let { product ->
                ProductQuickviewDialog(
                    product = product,
                    onDismiss = { showQuickviewByProduct = null }
                )
            }
        }
    }
}

@Composable
fun ProductBody(
    statusUiProductVM: DaftarProductUiState,
    products: List<DataProduct>,
    onProduct:(DataProduct) -> Unit,
    onEdit: (Int) -> Unit,
    onDelete: (DataProduct) -> Unit,
    retryAction:() -> Unit,
    selectedKategori: Kategori?,
    onSelectedKategori: (Kategori?) -> Unit,
    modifier: Modifier = Modifier
){
    when(statusUiProductVM){
        is DaftarProductUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { LoadScreen() }
        is DaftarProductUiState.Error -> ErorScreen(retryAction)
        is DaftarProductUiState.Success -> {
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
                if (products.isEmpty()){
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Produk tidak ditemukan", color = colorResource(R.color.pink2))
                    }
                }
                else{
                    ProductList(
                        itemProd = products,
                        onProd = onProduct,
                        onEdit = { onEdit(it.id) },
                        onDelete = onDelete,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun ProductList(
    itemProd: List<DataProduct>,
    onEdit: (DataProduct) -> Unit,
    onDelete: (DataProduct) -> Unit,
    onProd: (DataProduct) -> Unit,
    modifier : Modifier = Modifier
){
    if (itemProd.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Belum ada produk nih...",
                color = colorResource(R.color.pink2),
                fontWeight = FontWeight.Medium
            )
        }
    }
    else{
        LazyColumn(modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(
                items = itemProd,
                key = {it.id}
            ){product ->
                ProductCard(
                    product = product,
                    onEdit = {onEdit(product)},
                    onDelete = {onDelete(product)},
                    modifier = Modifier.clickable{onProd(product)}
                )
            }
        }
    }
}

@Composable
fun ProductCard(
    product: DataProduct,
    onEdit:() -> Unit,
    onDelete:() -> Unit,
    modifier: Modifier = Modifier
){
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.hijau1)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(modifier = modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically) {
            val imageUrl = imageBaseUrl()
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageUrl + product.image,
                    contentDescription = product.nama,
                    modifier = Modifier.padding(4.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
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
                Text(
                    text =" Stok: ${ product.stok }",
                    color = colorResource(R.color.pink2),
                    fontWeight = FontWeight.Light,
                    maxLines = 1
                )
            }
            Row {
                IconButton(
                    onClick = onEdit
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit",
                        tint = colorResource(R.color.pink2),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(Modifier.width(4.dp))
                IconButton(
                    onClick = { deleteConfirmationRequired = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "delete",
                        tint = colorResource(R.color.pink2),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
    if (deleteConfirmationRequired){
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                deleteConfirmationRequired = false
                onDelete()
            },
            onDeleteCancel = {deleteConfirmationRequired = false},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun imageBaseUrl(): String {
    val context = LocalContext.current.applicationContext as WhimsyWhisk
    return (context.container as BakeryContainer).imageUrl
}
@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(onDismissRequest = {},
        title = { Text(stringResource(R.string.attention)) },
        text = {Text(stringResource(R.string.tanya))},
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes))
            }
        })
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
fun ErorScreen(
    retryAction:() -> Unit,
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
fun LoadScreen(
    modifier: Modifier = Modifier
){
    AsyncImage(
        modifier = modifier.size(100.dp),
        model = R.drawable.load,
        contentDescription = stringResource(R.string.load)
    )
}