package com.example.finalproject_209.ui.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject_209.R
import com.example.finalproject_209.ui.view.Product.ErorScreen
import com.example.finalproject_209.ui.view.Product.LoadScreen
import com.example.finalproject_209.ui.view.customwidget.BakeryTopBar
import com.example.finalproject_209.ui.view.customwidget.BottomBarNav
import com.example.finalproject_209.viewmodel.provider.PenyediaViewModel
import com.example.finalproject_209.viewmodel.user.ProfileUiState
import com.example.finalproject_209.viewmodel.user.ProfileVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToProduct: () -> Unit,
    navigateToTrans: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileVM = viewModel(factory = PenyediaViewModel.Factory)
) {
    Scaffold(
        topBar = {
            BakeryTopBar(title = "My Profile", canNavigateBack = false)
        },
        bottomBar = {
            BottomBarNav(
                selectedTab = 3,
                navigateToHome = navigateToHome,
                navigateToProduct = navigateToProduct,
                navigateToTransaksi = navigateToTrans,
                navigateToUser = {  }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(colorResource(R.color.pink1))
                .padding(innerPadding)
        ) {
            when (val state = viewModel.profileUiState) {
                is ProfileUiState.Loading -> LoadScreen()
                is ProfileUiState.Error -> ErorScreen(retryAction = { viewModel.getProfile() })
                is ProfileUiState.Success -> {
                    ProfileContent(
                        user = state.user,
                        onLogout = { viewModel.logout(onLogoutClick) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileContent(user: com.example.finalproject_209.model.DataUser, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            tint = colorResource(R.color.pink2)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.hijau1))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                InfoRow(label = "Full Name", value = user.name)
                HorizontalDivider(color = colorResource(R.color.pink1), modifier = Modifier.padding(vertical = 12.dp))
                InfoRow(label = "Username", value = user.username)
                HorizontalDivider(color = colorResource(R.color.pink1), modifier = Modifier.padding(vertical = 12.dp))
                InfoRow(label = "Email Address", value = user.email)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout Session", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 12.sp, color = colorResource(R.color.pink2).copy(alpha = 0.7f))
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = colorResource(R.color.pink2))
    }
}