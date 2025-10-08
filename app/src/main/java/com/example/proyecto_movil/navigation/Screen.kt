package com.example.proyecto_movil.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")

    // Perfil por UID (String). Permitimos también Int para compatibilidad.
    object Profile : Screen("profile/{uid}") {
        fun createRoute(uid: String) = "profile/$uid"
        fun createRoute(uid: Int) = "profile/$uid" // compatibilidad con llamadas antiguas
    }

    object Album : Screen("album/{albumId}") {
        fun createRoute(albumId: Int) = "album/$albumId"
        fun createRoute(albumId: String) = "album/$albumId" // por si en algún sitio llega como String
    }

    object ContentArtist : Screen("content_artist/{artistId}") {
        fun createRoute(artistId: Int) = "content_artist/$artistId"
        fun createRoute(artistId: String) = "content_artist/$artistId"
    }

    object ContentUser : Screen("content/user")
    object Settings : Screen("settings")
    object EditProfile : Screen("editProfile")
    object AddReview : Screen("addReview")
}
