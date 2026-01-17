package com.example.finalproject_209.ui.customwidget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.finalproject_209.R
import com.example.finalproject_209.model.DataPesanan
import com.example.finalproject_209.model.DataProduct
import com.example.finalproject_209.model.Status
import com.example.finalproject_209.ui.view.imageBaseUrl
import com.example.finalproject_209.viewmodel.pesanan.ItemDetailJoin

@Composable
fun ProductQuickviewDialog(
    product: DataProduct,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.hijau1)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detail Product",
                        style = MaterialTheme.typography.titleMedium,
                        color = colorResource(R.color.pink2),
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = colorResource(R.color.pink2)
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(
                        bottom = 10.dp,
                        top = 10.dp),
                    thickness = 3.dp,
                    color = colorResource(R.color.pink2)
                )
                val imageUrl = imageBaseUrl()
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = imageUrl + product.image,
                        contentDescription = product.nama,
                        modifier = Modifier.padding(12.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = product.nama,
                        style = MaterialTheme.typography.headlineSmall,
                        color = colorResource(R.color.pink2),
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = product.desc,
                        textAlign = TextAlign.Start,
                        color = colorResource(R.color.pink2),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Rp ${product.price}",
                        style = MaterialTheme.typography.titleLarge,
                        color = colorResource(R.color.pink2),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Stok: ${product.stok}",
                        style = MaterialTheme.typography.titleLarge,
                        color = colorResource(R.color.pink2),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TransaksiQuickviewDialog(
    pesanan: DataPesanan,
    items: List<ItemDetailJoin>,
    onDismiss: () -> Unit,
    loadItems: (Int) -> Unit
) {
    LaunchedEffect(pesanan.id) {
        loadItems(pesanan.id)
    }
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.hijau1)),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            val scrollState = androidx.compose.foundation.rememberScrollState()
            Column(modifier = Modifier.padding(16.dp)
                .verticalScroll(scrollState)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Detail Nota #${pesanan.id}", fontWeight = FontWeight.Bold, color = colorResource(R.color.pink2))
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = colorResource(R.color.pink2))
                    }
                }

                Text("Customer: ${pesanan.namaCust}", fontSize = 14.sp)
                HorizontalDivider(Modifier.padding(vertical = 8.dp), thickness = 2.dp, color = colorResource(R.color.pink2))

                Text("Daftar Pesanan:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                when {
                    items.isEmpty() -> {
                        Text(
                            text = "Memuat item pesanan...",
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = colorResource(R.color.pink2)
                        )
                    }
                    else -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            items.forEach { item ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("${item.detail.qty}x ${item.namaProduk}")
                                    Text("Rp ${item.detail.subtotal}")
                                }
                            }
                        }
                    }
                }
                HorizontalDivider(Modifier.padding(vertical = 8.dp), color = colorResource(R.color.pink2))

                // TOTAL
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("TOTAL AKHIR", fontWeight = FontWeight.ExtraBold, color = colorResource(R.color.pink2))
                    Text("Rp ${pesanan.total_harga}", fontWeight = FontWeight.ExtraBold, color = colorResource(R.color.pink2))
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Status: ${pesanan.status.name.uppercase()}",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold,
                    color = if(pesanan.status == Status.finish) Color(0xFF4CAF50) else Color.Red
                )
            }
        }
    }
}