package com.example.finalproject_209.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject_209.R
import com.example.finalproject_209.ui.theme.InterFont
import com.example.finalproject_209.viewmodel.provider.PenyediaViewModel
import com.example.finalproject_209.viewmodel.user.LoginVM
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    onSubmit: () -> Unit,
    viewModel: LoginVM = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiStateLogin = viewModel.uiStateLogin
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
//    LaunchedEffect(uiStateLogin.user) {
//        if (uiStateLogin.user != null) {
//            onSubmit()
//        }
//    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.pink1))
        ) {

            // BOX ATAS (BACKGROUND)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 50.dp,
                            bottomEnd = 50.dp
                        )
                    )
                    .background(colorResource(R.color.pink2))
            )

            // CARD LOGIN (NIMPA BOX ATAS)
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = 230.dp),
                colors = CardDefaults.cardColors(colorResource(R.color.hijau1)),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Login",
                        fontFamily = InterFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        color = colorResource(R.color.pink2)
                    )

                    Spacer(Modifier.height(20.dp))

                    // USERNAME
                    TextField(
                        value = uiStateLogin.detailLogin.username,
                        onValueChange = {
                            viewModel.updateuiState(
                                uiStateLogin.detailLogin.copy(username = it)
                            )
                            viewModel.resetError()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = uiStateLogin.errorState.username != null,
                        placeholder = { Text("Username",color = colorResource(R.color.pink2)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null,
                                tint = colorResource(R.color.pink2)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colorResource(R.color.hijau1),
                            unfocusedContainerColor = colorResource(R.color.hijau1),
                            focusedTextColor = colorResource(R.color.pink2),
                            unfocusedTextColor = colorResource(R.color.pink2)
                        )
                    )

                    uiStateLogin.errorState.username?.let {
                        Text(it, color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(Modifier.height(12.dp))

                    // PASSWORD
                    TextField(
                        value = uiStateLogin.detailLogin.password,
                        onValueChange = {
                            viewModel.updateuiState(
                                uiStateLogin.detailLogin.copy(password = it)
                            )
                            viewModel.resetError()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = uiStateLogin.errorState.password != null,
                        placeholder = { Text("Password", color = colorResource(R.color.pink2)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = null,
                                tint = colorResource(R.color.pink2)
                            )
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = image,
                                    contentDescription = if (passwordVisible) "Sembunyikan password" else "Tampilkan password",
                                    tint = colorResource(R.color.pink2)
                                )
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colorResource(R.color.hijau1),
                            unfocusedContainerColor = colorResource(R.color.hijau1),
                            focusedTextColor = colorResource(R.color.pink2),
                            unfocusedTextColor = colorResource(R.color.pink2)
                        )
                    )
                    uiStateLogin.errorState.password?.let {
                        Text(it, color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(Modifier.height(24.dp))

                    // BUTTON DI DALAM CARD
                    Button(
                        onClick = {
                            viewModel.login(uiStateLogin.detailLogin)
                            {
                                success ->
                                if (success){
                                    onSubmit()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.pink2)
                        )
                    ) {
                        Text(
                            text = "Login",
                            color = colorResource(R.color.hijau1),
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }
            }
        }
    }
}
