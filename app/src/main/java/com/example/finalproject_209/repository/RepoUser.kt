package com.example.finalproject_209.repository

import com.example.finalproject_209.model.DataUser
import com.example.finalproject_209.model.LoginRequest
import com.example.finalproject_209.model.LoginResponse
import com.example.finalproject_209.service.ServiceApiBakery

interface RepositoryDataUser{
    suspend fun getUser(): List<DataUser>?
    suspend fun getUserById(id: Int): DataUser
    suspend fun login(request: LoginRequest): LoginResponse?
}

class NetworkUserRepo(
    private val serviceApiBakery: ServiceApiBakery
): RepositoryDataUser{
    override suspend fun getUser(): List<DataUser> {
        return serviceApiBakery.getUser()
    }

    override suspend fun login(request: LoginRequest): LoginResponse {
        return serviceApiBakery.login(request)
    }

    override suspend fun getUserById(id: Int): DataUser {
        return serviceApiBakery.getUserById(id)
    }
}