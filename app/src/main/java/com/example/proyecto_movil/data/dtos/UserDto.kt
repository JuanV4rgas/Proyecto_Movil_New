package com.example.proyecto_movil.data.dtos

import com.example.proyecto_movil.data.UserInfo

/**
 * DTO que refleja exactamente la respuesta de la API.
 */
data class UserProfileDto(
    val id: Int,
    val username: String,
    val profile_pic: String, // URL
    val bio: String,
    val followers: Int,
    val following: Int,
    val createdAt: String,
    val updatedAt: String,
)

/**
 * Mapeo de DTO -> UI model.
 * createdAt/updatedAt no están en UserUI; si los necesitás para UI, agregalos allí.
 */
fun UserProfileDto.toUserUI(): UserInfo {
    return UserInfo(
        id = id,
        username = username,
        profileImageUrl = profile_pic,
        bio = bio,
        followers = followers,
        following = following,
        playlists = emptyList()
    )
}
