package com.example.proyecto_movil.data.repository

import coil.network.HttpException
import com.example.proyecto_movil.data.UserInfo
import com.example.proyecto_movil.data.datasource.impl.retrofit.UserRetrofitDataSourceImpl
import com.example.proyecto_movil.data.dtos.UpdateUserDto
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRetrofitDataSourceImpl
) {

    /** Obtiene un usuario por su ID desde el backend */
    suspend fun getUserById(id: String): Result<UserInfo> = try {
        val user = userRemoteDataSource.getUserById(id)
        Result.success(user)
    } catch (e: HttpException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }

    /** Actualiza la información de un usuario */
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
}
