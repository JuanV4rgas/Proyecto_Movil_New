package com.example.proyecto_movil.ui.Screens.UserProfile

import com.example.proyecto_movil.data.AlbumInfo
import com.example.proyecto_movil.data.ReviewInfo
import com.example.proyecto_movil.data.UserInfo

data class UserProfileState(
    val user: UserInfo? = null,
    val username: String = "",
    val avatarUrl: String = "",
    val followers: Int = 0,
    val following: Int = 0,
    val favoriteAlbums: List<AlbumInfo> = emptyList(),
    val reviews: List<ReviewInfo> = emptyList(),

    // Estado UI
    val isLoading: Boolean = false,
    val error: String? = null,

    // Navegación
    val navigateBack: Boolean = false,
    val navigateToSettings: Boolean = false,
    val navigateToEditProfile: Boolean = false,
    val openAlbumId: Int? = null,
    val openReview: ReviewInfo? = null
)
