package com.example.finalproject_209.viewmodel.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject_209.model.DataUser
import com.example.finalproject_209.model.LoginRequest
import com.example.finalproject_209.repository.RepositoryDataUser
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginVM(
    private val repositoryDataUser: RepositoryDataUser
) : ViewModel() {
    private val _message = mutableStateOf<String?>(null)
    val message: String? get() = _message.value

    private val _isErrorMessage = mutableStateOf(false)
    val isErrorMessage: Boolean get() = _isErrorMessage.value

    fun dismissMessage() {
        _message.value = null
    }
    var uiStateLogin by mutableStateOf(UIStateLogin())
        private set

    fun updateuiState(detailLogin: DetailLogin) {
        uiStateLogin = uiStateLogin.copy(
            detailLogin = detailLogin,
            errorState = FormErrorLogin(),
            isSubmitted = false
        )
    }

    fun login(
        detailLogin: DetailLogin,
        onResult:(Boolean)-> Unit
    ){
        viewModelScope.launch {
            val success = doLogin(detailLogin)
            onResult(success)
        }
    }

    fun resetError(){
        uiStateLogin=uiStateLogin.copy(
            errorState = FormErrorLogin(),
            isSubmitted = false
        )
    }

    private suspend fun doLogin(login: DetailLogin): Boolean {
        val errorState = validasiinput(login)

        uiStateLogin = uiStateLogin.copy(
            detailLogin = login,
            errorState = errorState,
            isSubmitted = true
        )

        if (!errorState.isValid) {
            _message.value = "Username dan password wajib diisi"
            _isErrorMessage.value = true
            return false
        }
        return try {
            val response = repositoryDataUser.login(
                LoginRequest(
                    username = login.username,
                    password = login.password
                )
            )

            if (response?.user != null) {
                uiStateLogin = uiStateLogin.copy(
                    user = response.user,
                    errorState = FormErrorLogin()
                )
                _message.value = null
                _isErrorMessage.value = false
                true
            } else {
                _message.value = "Username atau password salah"
                _isErrorMessage.value = true

                uiStateLogin = uiStateLogin.copy(
                    errorState = FormErrorLogin(
                        username = "Username atau password salah",
                        password = "Username atau password salah"
                    )
                )
                false
            }

        } catch (e: HttpException) {
            _message.value = "Username atau password salah"
            _isErrorMessage.value = true

            uiStateLogin = uiStateLogin.copy(
                errorState = FormErrorLogin(
                    username = "Username atau password salah",
                    password = "Username atau password salah"
                )
            )
            false

        } catch (e: Exception) {
            _message.value = "Terjadi kesalahan server"
            _isErrorMessage.value = true
            false
        }
    }



    private fun validasiinput(login: DetailLogin): FormErrorLogin {
        return FormErrorLogin(
            username = if (login.username.isBlank()) "Username wajib diisi" else null,
            password = if (login.password.isBlank()) "Password wajib diisi" else null
        )
    }
}

data class UIStateLogin(
    val detailLogin: DetailLogin = DetailLogin(),
    val errorState: FormErrorLogin = FormErrorLogin(),
    val isSubmitted: Boolean = false,
    val user: DataUser? = null
)

data class DetailLogin(
    val username: String = "",
    val password: String = ""
)

data class FormErrorLogin(
    val username: String? = null,
    val password: String? = null
) {
    val isValid: Boolean
        get() = username == null && password == null
}