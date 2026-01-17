package com.example.finalproject_209.ui.view.pesanan_item

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.finalproject_209.R
import com.example.finalproject_209.ui.view.ErrorScreen
import com.example.finalproject_209.ui.view.LoadingScreen
import com.example.finalproject_209.ui.view.customwidget.BakeryMessageBar
import com.example.finalproject_209.ui.view.customwidget.BakeryTopBar
import com.example.finalproject_209.ui.view.imageBaseUrl
import com.example.finalproject_209.ui.view.route.pesanan_item.DestinasiItemPesanan
import com.example.finalproject_209.viewmodel.pesanan_item.DaftarItemPesananUiState
import com.example.finalproject_209.viewmodel.pesanan_item.DaftarPesananItemVM
import com.example.finalproject_209.viewmodel.pesanan_item.Join
import com.example.finalproject_209.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarPesananItemScreen(
    navigateBack: () -> Unit,
    onPaymentSuccess: () -> Unit,
    viewModel: DaftarPesananItemVM = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.listItemPesanan
    val context = LocalContext.current
    val message by viewModel.message.collectAsState()
    val isError by viewModel.isErrorMessage.collectAsState()
    Scaffold(
        topBar = {
            BakeryTopBar(
                title = stringResource(DestinasiItemPesanan.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.pink1))
                .padding(innerPadding)
        ){
            when (uiState) {
                is DaftarItemPesananUiState.Loading -> LoadingScreen()
                is DaftarItemPesananUiState.Error -> ErrorScreen(retryAction = { viewModel.LoadPesananItem() })
                is DaftarItemPesananUiState.Success -> {
                    if (uiState.join.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Keranjang belanja kosong", color = colorResource(R.color.pink2))
                        }
                    } else {
                        DaftarPesananItemBody(
                            items = uiState.join,
                            totalHarga = viewModel.getTotal(uiState.join),
                            onIncrease = { join ->
                                join.product?.let { viewModel.IncreaseQty(join.pesananItem, it.price) }
                            },
                            onDecrease = { join ->
                                join.product?.let { viewModel.DecreaseQty(join.pesananItem, it.price) }
                            },
                            onUpdateCustName = { id, name -> viewModel.updateCustName(id, name) },
                            onCheckout = {
                                viewModel.checkout()
                                android.widget.Toast.makeText(context, "Pembayaran Berhasil!", android.widget.Toast.LENGTH_SHORT).show()
                                onPaymentSuccess()
                            }
                        )
                    }
                }
            }
            message?.let {
                BakeryMessageBar(
                    message = it,
                    isError = isError,
                    onDismiss = { viewModel.dismissMessage() }
                )
            }
        }
    }
}

@Composable
fun DaftarPesananItemBody(
    items: List<Join>,
    totalHarga: Int,
    onIncrease: (Join) -> Unit,
    onDecrease: (Join) -> Unit,
    onUpdateCustName: (Int, String) -> Unit,
    onCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pesananId = items.firstOrNull()?.pesanan?.id ?: 0
    var custName by remember(items.firstOrNull()?.pesanan?.namaCust) {
        mutableStateOf(items.firstOrNull()?.pesanan?.namaCust ?: "")
    }
    Column(
        modifier = modifier
            .background(colorResource(R.color.pink1))
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Text("Item", Modifier.weight(1.5f), color = colorResource(R.color.pink2), fontWeight = FontWeight.Bold)
            Text("Qty", Modifier.weight(1f), color = colorResource(R.color.pink2), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text("Price", Modifier.weight(1f), color = colorResource(R.color.pink2), fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
        }
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items, key = { it.pesananItem.id }) { item ->
                ItemPesananCard(item, onIncrease, onDecrease)
                Spacer(Modifier.height(8.dp))
            }
        }
        val pesananId = items.firstOrNull()?.pesanan?.id ?: 0
        var custName by remember { mutableStateOf(items.firstOrNull()?.pesanan?.namaCust ?: "") }

        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
            Text("Nama Cust", color = colorResource(R.color.pink2), fontSize = 12.sp)
            TextField(
                value = custName,
                onValueChange = {
                    custName = it
                    onUpdateCustName(pesananId, it)
                },
                placeholder = { Text("Input Nama Customer") },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total", color = colorResource(R.color.pink2), fontWeight = FontWeight.Bold)
            Text("Rp $totalHarga", color = colorResource(R.color.pink2), fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = onCheckout,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.pink2))
        ) {
            Text("Payment", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
@Composable
fun ItemPesananCard(
    item: Join,
    onIncrease: (Join) -> Unit,
    onDecrease: (Join) -> Unit
) {
    val imageUrl = imageBaseUrl()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageUrl + (item.product?.image ?: ""),
            contentDescription = null,
            modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)).background(Color.White),
            contentScale = ContentScale.Fit
        )
        Column(modifier = Modifier.weight(1.5f).padding(start = 8.dp)) {
            Text(item.product?.nama ?: "Unknown", color = colorResource(R.color.pink2), fontWeight = FontWeight.Bold)
            Text("Rp ${item.product?.price ?: 0}", color = colorResource(R.color.pink2), fontSize = 12.sp)
        }
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { onDecrease(item) }, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Remove, contentDescription = null, tint = colorResource(R.color.pink2))
            }
            Text("${item.pesananItem.qty}", modifier = Modifier.padding(horizontal = 8.dp), color = colorResource(R.color.pink2))
            IconButton(onClick = { onIncrease(item) }, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Add, contentDescription = null, tint = colorResource(R.color.pink2))
            }
        }
        Text(
            "Rp ${item.pesananItem.subtotal}",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            color = colorResource(R.color.pink2)
        )
    }
}