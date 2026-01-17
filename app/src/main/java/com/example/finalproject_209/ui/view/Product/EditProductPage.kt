package com.example.finalproject_209.ui.view.Product

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.finalproject_209.R
import com.example.finalproject_209.model.DetailProduct
import com.example.finalproject_209.model.Kategori
import com.example.finalproject_209.model.UiStateProduct
import com.example.finalproject_209.model.ValidasiProductField
import com.example.finalproject_209.ui.view.customwidget.BakeryMessageBar
import com.example.finalproject_209.ui.view.customwidget.BakeryTopBar
import com.example.finalproject_209.ui.view.route.product.DestinasiEditProduct
import com.example.finalproject_209.viewmodel.product.EditProductVM
import com.example.finalproject_209.viewmodel.provider.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProductVM = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState = viewModel.uiStateProduct
    val message by viewModel.message.collectAsState()
    val isError by viewModel.isErrorMessage.collectAsState()
    Scaffold(
        topBar = {
            BakeryTopBar(
                title = stringResource(DestinasiEditProduct.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            EditProductBody(
                uiStateProduct = uiState,
                onValueChange = viewModel::updateUiState,
                onSaveClick = {
                    coroutineScope.launch {
                        if (viewModel.editSatuProduct(context)) {
                            android.widget.Toast.makeText(context, "Update Berhasil!", android.widget.Toast.LENGTH_SHORT).show()
                            navigateBack()
                        } else {
                            android.widget.Toast.makeText(context, "Gagal Update!", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.verticalScroll(rememberScrollState())
            )
            message?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                ) {
                    BakeryMessageBar(
                        message = it,
                        isError = isError,
                        onDismiss = { viewModel.dismissMessage() }
                    )
                }
            }
        }
    }
}
@Composable
fun EditProductBody(
    uiStateProduct: UiStateProduct,
    onValueChange: (DetailProduct) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FormEditProduct(
            detailProduct = uiStateProduct.detailProduct,
            validation = uiStateProduct.validation,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.pink2)
            )
        ) {
            Text("Simpan Produk", color = Color.White)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormEditProduct(
    detailProduct: DetailProduct,
    validation: ValidasiProductField,
    modifier: Modifier = Modifier,
    onValueChange:(DetailProduct) -> Unit = {},
    enabled: Boolean =true
){
    var expanded by remember { mutableStateOf(false) }
    Column {
        OutlinedTextField(
            value = detailProduct.nama,
            onValueChange = { onValueChange(detailProduct.copy(nama = it)) },
            label = { Text(stringResource(R.string.prod_name)) },
            modifier = Modifier.fillMaxWidth(),
            isError = validation.errorNama != null,
            supportingText = { validation.errorNama?.let { Text(it, color = Color.Red) } },
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = detailProduct.desc,
            onValueChange = { onValueChange(detailProduct.copy(desc = it)) },
            label = { Text(stringResource(R.string.prod_desc)) },
            modifier = Modifier.fillMaxWidth(),
            isError = validation.errorDesc != null,
            supportingText = { validation.errorDesc?.let { Text(it, color = Color.Red) } },
            enabled = enabled
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = if (detailProduct.price == 0) "" else detailProduct.price.toString(),
                onValueChange = { onValueChange(detailProduct.copy(price = it.toIntOrNull() ?: 0)) },
                label = { Text(stringResource(R.string.prod_price)) },
                modifier = Modifier.weight(1f),
                isError = validation.errorPrice != null,
                supportingText = { validation.errorPrice?.let { Text(it, color = Color.Red) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = enabled
            )
            OutlinedTextField(
                value = if (detailProduct.stok == 0) "" else detailProduct.stok.toString(),
                onValueChange = { onValueChange(detailProduct.copy(stok = it.toIntOrNull() ?: 0)) },
                label = { Text(stringResource(R.string.prod_stok)) },
                modifier = Modifier.weight(1f),
                isError = validation.errorStok != null,
                supportingText = { validation.errorStok?.let { Text(it, color = Color.Red) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = enabled
            )
        }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = detailProduct.kategori.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Kategori") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Kategori.values().forEach { kategori ->
                    DropdownMenuItem(
                        text = { Text(kategori.name) },
                        onClick = {
                            onValueChange(detailProduct.copy(kategori = kategori))
                            expanded = false
                        }
                    )
                }
            }
        }
        ImagePick(
            imageUrl = detailProduct.image,
            onImageSelected = { onValueChange(detailProduct.copy(image = it)) },
            errorMessage = validation.errorImage
        )
    }
}

@Composable
fun ImagePick(
    imageUrl: String,
    onImageSelected: (String) -> Unit,
    errorMessage: String?
) {
    // Launcher untuk galeri
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it.toString()) }
    }

    Column {
        Text("Foto Produk", fontWeight = FontWeight.Bold)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl.isBlank()) {
                Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(40.dp))
            } else {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        errorMessage?.let { Text(it, color = Color.Red, fontSize = 12.sp) }
    }
}