package com.example.proyecto_movil.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserInfo(
    val id: Int,
    val username: String,
    val profileImageUrl: String,
    val bio: String = "",
    val followers: Int = 0,
    val following: Int = 0,
    val playlists: List<PlaylistInfo> = emptyList()
) : Parcelable
