package com.example.proyecto_movil.ui.Screens.UserProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_movil.data.AlbumInfo
import com.example.proyecto_movil.data.ReviewInfo
import com.example.proyecto_movil.data.UserInfo
import com.example.proyecto_movil.data.repository.UserRepository
import com.example.proyecto_movil.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UserProfileState> = MutableStateFlow(UserProfileState())
    val uiState: StateFlow<UserProfileState> = _uiState

    /**
     * Carga el usuario y sus reseñas desde el backend
     */
    fun loadUser(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val userResult = userRepository.getUserById(userId)
                val reviewsResult = reviewRepository.getReviewsByUserId(userId)

                if (userResult.isSuccess) {
                    val user = userResult.getOrNull()
                    val reviews = reviewsResult.getOrElse { emptyList() }

                    if (user != null) {
                        val fav = user.playlists.firstOrNull()?.albums ?: emptyList()

                        _uiState.update {
                            it.copy(
                                user = user,
                                username = user.username,
                                avatarUrl = user.profileImageUrl,
                                followers = user.followers,
                                following = user.following,
                                favoriteAlbums = fav,
                                reviews = reviews,
                                isLoading = false,
                                error = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                error = "Usuario no encontrado",
                                isLoading = false
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            error = userResult.exceptionOrNull()?.message
                                ?: "Error al cargar usuario",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "Error desconocido", isLoading = false)
                }
            }
        }
    }

    /**
     * Para inicializar datos si ya tienes el usuario y las reseñas cargadas.
     */
    fun setInitialData(user: UserInfo, reviews: List<ReviewInfo>) {
        val fav = user.playlists.firstOrNull()?.albums ?: emptyList()
        _uiState.update {
            it.copy(
                user = user,
                username = user.username,
                avatarUrl = user.profileImageUrl,
                followers = user.followers,
                following = user.following,
                favoriteAlbums = fav,
                reviews = reviews,
                isLoading = false,
                error = null
            )
        }
    }

    // ---- Acciones de UI ----
    fun onBackClicked() = _uiState.update { s -> s.copy(navigateBack = true) }
    fun onSettingsClicked() = _uiState.update { s -> s.copy(navigateToSettings = true) }
    fun onEditProfileClicked() = _uiState.update { s -> s.copy(navigateToEditProfile = true) }
    fun onAlbumClicked(album: AlbumInfo) = _uiState.update { s -> s.copy(openAlbumId = album.id) }
    fun onReviewClicked(review: ReviewInfo) = _uiState.update { s -> s.copy(openReview = review) }

    // ---- Consumidores ----
    fun consumeBack() = _uiState.update { s -> s.copy(navigateBack = false) }
    fun consumeSettings() = _uiState.update { s -> s.copy(navigateToSettings = false) }
    fun consumeEdit() = _uiState.update { s -> s.copy(navigateToEditProfile = false) }
    fun consumeOpenAlbum() = _uiState.update { s -> s.copy(openAlbumId = null) }
    fun consumeOpenReview() = _uiState.update { s -> s.copy(openReview = null) }
}
