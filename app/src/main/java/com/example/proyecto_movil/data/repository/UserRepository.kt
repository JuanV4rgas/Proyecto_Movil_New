package com.example.proyecto_movil.data.repository

import coil.network.HttpException
import com.example.proyecto_movil.data.UserInfo
import com.example.proyecto_movil.data.datasource.impl.retrofit.UserRetrofitDataSourceImpl
import com.example.proyecto_movil.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.proyecto_movil.data.dtos.UpdateUserDto
import com.example.proyecto_movil.data.dtos.RegisterUserDto
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRetrofitDataSourceImpl,
    private val userFirestoreDataSource: UserFirestoreDataSourceImpl
) {

    suspend fun getUserById(id: String): Result<UserInfo> = try {
        val user = userRemoteDataSource.getUserById(id)
        Result.success(user)
    } catch (e: HttpException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateUser(
        id: String,
        username: String,
        bio: String,
        profilePic: String?
    ): Result<UserInfo> = try {
        val updateDto = UpdateUserDto(
            username = username,
            bio = bio,
            profile_pic = profilePic
        )
        val updatedUser = userRemoteDataSource.updateUser(id, updateDto)
        Result.success(updatedUser)
    } catch (e: HttpException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun registerUser(
        username: String,
        name: String?,
        bio: String?,
        userId: String
    ): Result<Unit> = try {
        val registerUserDto = RegisterUserDto(
            userName = username,
            name = name,
            bio = bio
        )
        userFirestoreDataSource.registerUser(registerUserDto, userId)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
