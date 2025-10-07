package com.example.proyecto_movil.data.datasource.impl.firestore

import com.example.proyecto_movil.data.UserInfo
import com.example.proyecto_movil.data.datasource.UserRemoteDataSource
import com.example.proyecto_movil.data.dtos.RegisterUserDto
import com.example.proyecto_movil.data.dtos.ReviewDto
import com.example.proyecto_movil.data.dtos.UpdateUserDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImpl @Inject constructor(private val db: FirebaseFirestore) : UserRemoteDataSource {
    override suspend fun getUserById(id: String): UserInfo {
        TODO("Not yet implemented")
    }

    override suspend fun getUserReviews(id: String): List<ReviewDto> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(
        id: String,
        userDto: UpdateUserDto
    ): UserInfo {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String) {
        val docRef = db.collection("users").document(userId)// se crea un documento
        docRef.set(registerUserDto).await()//insertando el documento
    }
}