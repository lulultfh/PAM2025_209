package com.example.finalproject_209.ui.view.pesanan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject_209.R
import com.example.finalproject_209.model.DataPesanan
import com.example.finalproject_209.model.Status
import com.example.finalproject_209.ui.customwidget.TransaksiQuickviewDialog
import com.example.finalproject_209.ui.view.Product.ErorScreen
import com.example.finalproject_209.ui.view.Product.LoadScreen
import com.example.finalproject_209.ui.view.customwidget.BakeryTopBar
import com.example.finalproject_209.ui.view.customwidget.BottomBarNav
import com.example.finalproject_209.ui.view.route.pesanan.DestinasiDaftarPesanan
import com.example.finalproject_209.viewmodel.pesanan.DaftarPesananUiState
import com.example.finalproject_209.viewmodel.pesanan.DaftarPesananVM
import com.example.finalproject_209.viewmodel.pesanan.Join
import com.example.finalproject_209.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PesananScreen(
    navigateToHome: () -> Unit,
    navigateToProduct: () -> Unit,
    navigateToTrans: () -> Unit,
    navigateToUser: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DaftarPesananVM = viewModel(factory = PenyediaViewModel.Factory)
) {
    var selectedPesanan by remember { mutableStateOf<DataPesanan?>(null) }
    var showQuickview by remember { mutableStateOf(false) }
    val uiState = viewModel.listPesanan
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            BakeryTopBar(
                title = stringResource(DestinasiDaftarPesanan.titleRes),
                isHome = false,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomBarNav(
                selectedTab = 2, // Index untuk Transaksi
                navigateToHome = navigateToHome,
                navigateToProduct = navigateToProduct,
                navigateToTransaksi = navigateToTrans,
                navigateToUser = navigateToUser
            )
        }
    ) { innerPadding ->
        PesananBody(
            uiState = uiState,
            onDelete = { join -> viewModel.hapusPesanan(join.pesanan.id) },
            onPesananClick = { pesanan ->
                selectedPesanan = pesanan
                viewModel.loadDetailItem(pesanan.id) // Ambil data pesanan_item
                showQuickview = true
            },
            onStatusChange = { join, status -> viewModel.updateStatus(join.pesanan, status) },
            onFilterStatus = { viewModel.filterByStatus(it) },
            retryAction = { viewModel.LoadPesanan() },
            modifier = Modifier
                .background(colorResource(R.color.pink1))
                .padding(innerPadding)
        )
        if (showQuickview && selectedPesanan != null) {
            TransaksiQuickviewDialog(
                pesanan = selectedPesanan!!,
                items = viewModel.listDetailItem,
                loadItems = { id -> viewModel.loadDetailItem(id) },
                onDismiss = { showQuickview = false }
            )
        }
    }
}

@Composable
fun PesananBody(
    onPesananClick: (DataPesanan) -> Unit,
    uiState: DaftarPesananUiState,
    onDelete: (Join) -> Unit,
    onStatusChange: (Join, Status) -> Unit,
    onFilterStatus: (Status) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is DaftarPesananUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { LoadScreen() }
        is DaftarPesananUiState.Error -> ErorScreen(retryAction)
        is DaftarPesananUiState.Success -> {
            Column(modifier = modifier.fillMaxSize()) {
                // Baris Filter Status
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Status.values().forEach { status ->
                        FilterChip(
                            selected = false, // Bisa dikembangkan dengan state aktif di VM
                            onClick = { onFilterStatus(status) },
                            label = { Text(status.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White,
                                selectedContainerColor = colorResource(R.color.hijau1)
                            )
                        )
                    }
                }

                PesananList(
                    itemPesanan = uiState.join,
                    onDelete = onDelete,
                    onStatusChange = onStatusChange,
                    onPesananClick = onPesananClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun PesananList(
    itemPesanan: List<Join>,
    onPesananClick:(DataPesanan) -> Unit,
    onDelete: (Join) -> Unit,
    onStatusChange: (Join, Status) -> Unit,
    modifier: Modifier = Modifier
) {
    if (itemPesanan.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Belum ada transaksi nih...",
                color = colorResource(R.color.pink2),
                fontWeight = FontWeight.Medium
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(
                items = itemPesanan,
                key = { it.pesanan.id }
            ) { item ->
                PesananCard(
                    item = item,
                    onDelete = { onDelete(item) },
                    onStatusChange = { newStatus -> onStatusChange(item, newStatus) },
                    modifier = Modifier.clickable { onPesananClick(item.pesanan) }
                )
            }
        }
    }
}
@Composable
fun PesananCard(
    item: Join,
    onDelete:() -> Unit,
    onStatusChange: (Status) -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.hijau1)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nota: #${item.pesanan.id}",
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.pink2)
                    )
                    Text(
                        text = item.pesanan.namaCust,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorResource(R.color.pink2)
                    )
                    Text(text = "Tgl: ${item.pesanan.tanggal}", fontSize = 12.sp)
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
                Surface(
                    color = if (item.pesanan.status == Status.finish) Color(0xFF4CAF50) else colorResource(R.color.pink2),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = item.pesanan.status.name.uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Divider(Modifier.padding(vertical = 8.dp), color = colorResource(R.color.pink1))
            if (item.pesanan.status == Status.process) {
                Button(
                    onClick = { onStatusChange(Status.finish) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.pink2))
                ) {
                    Text("Selesaikan Pesanan")
                }
            }
        }
    }
}