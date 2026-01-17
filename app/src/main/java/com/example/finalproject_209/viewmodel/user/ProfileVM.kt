package com.example.finalproject_209.viewmodel.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject_209.model.DataUser
import com.example.finalproject_209.repository.RepositoryDataUser
import kotlinx.coroutines.launch

sealed class ProfileUiState {
    data class Success(val user: DataUser) : ProfileUiState()
    object Error : ProfileUiState()
    object Loading : ProfileUiState()
}

class ProfileVM(
    private val repositoryDataUser: RepositoryDataUser
) : ViewModel() {

    var profileUiState: ProfileUiState by mutableStateOf(ProfileUiState.Loading)
        private set

    init {
        getProfile()
    }

    fun getProfile() {
        viewModelScope.launch {
            profileUiState = ProfileUiState.Loading
            try {
                val users = repositoryDataUser.getUser()
                if (!users.isNullOrEmpty()) {
                    profileUiState = ProfileUiState.Success(users.first())
                } else {
                    profileUiState = ProfileUiState.Error
                }
            } catch (e: Exception) {
                profileUiState = ProfileUiState.Error
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            onLogoutSuccess()
        }
    }
}