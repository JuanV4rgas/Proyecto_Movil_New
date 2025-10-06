package com.example.proyecto_movil.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyecto_movil.data.AlbumInfo
import com.example.proyecto_movil.ui.theme.Proyecto_movilTheme

// Screens + ViewModels
import com.example.proyecto_movil.ui.Screens.Welcome.WelcomeScreen
import com.example.proyecto_movil.ui.Screens.Welcome.WelcomeViewModel
import com.example.proyecto_movil.ui.Screens.Login.LoginScreen
import com.example.proyecto_movil.ui.Screens.Login.LoginViewModel
import com.example.proyecto_movil.ui.Screens.Register.RegisterScreen
import com.example.proyecto_movil.ui.Screens.Register.RegisterViewModel
import com.example.proyecto_movil.uiViews.homePage.HomeScreen
import com.example.proyecto_movil.ui.Screens.Home.HomeViewModel
import com.example.proyecto_movil.ui.Screens.UserProfile.UserProfileScreen
import com.example.proyecto_movil.ui.Screens.UserProfile.UserProfileViewModel
import com.example.proyecto_movil.ui.Screens.Settings.SettingsScreen
import com.example.proyecto_movil.ui.Screens.Settings.SettingsViewModel
import com.example.proyecto_movil.ui.Screens.Content.ContentScreen
import com.example.proyecto_movil.ui.Screens.Content.ContentViewModel
import com.example.proyecto_movil.ui.Screens.AddReview.AddReviewScreen
import com.example.proyecto_movil.ui.Screens.AddReview.AddReviewViewModel
import com.example.proyecto_movil.ui.Screens.EditProfile.EditarPerfilScreen
import com.example.proyecto_movil.ui.Screens.EditProfile.EditProfileViewModel
import com.example.proyecto_movil.ui.Screens.AlbumReviews.AlbumReviewScreen

// ---------------------- Rutas ----------------------
/*
sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Profile : Screen("profile/{userId}") {
        fun createRoute(userId: Int) = "profile/$userId"
    }
    object Album : Screen("album/{albumId}") {
        fun createRoute(albumId: Int) = "album/$albumId"
    }
    object ContentArtist : Screen("content_artist/{artistId}") {
        fun createRoute(artistId: Int) = "content_artist/$artistId"
    }
    object ContentUser : Screen("content/user")
    object Settings : Screen("settings")
    object EditProfile : Screen("editProfile")
    object AddReview : Screen("addReview")
}

*/
// ---------------------- NavHost ----------------------
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = Screen.Welcome.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        /* WELCOME */
        composable(Screen.Welcome.route) {
            val vm: WelcomeViewModel = hiltViewModel()
            WelcomeScreen(
                viewModel = vm,
                onStartClick = { navController.navigate(Screen.Login.route) }
            )
        }

        /* LOGIN */
        composable(Screen.Login.route) {
            val vm: LoginViewModel = hiltViewModel()
            val state = vm.uiState.collectAsState().value

            if (state.navigateAfterLogin) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                        launchSingleTop = true
                    }
                    vm.consumeAfterLogin()
                }
            }

            LoginScreen(
                viewModel = vm,
                onBack = { navController.navigateUp() },
                onRegister = { navController.navigate(Screen.Register.route) },
                onForgotPassword = { /* TODO */ }
            )
        }

        /* REGISTER */
        composable(Screen.Register.route) {
            val vm: RegisterViewModel = hiltViewModel()
            val state = vm.uiState.collectAsState().value

            if (state.navigateAfterRegister) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                        launchSingleTop = true
                    }
                    vm.consumeNavigation()
                }
            }

            RegisterScreen(
                viewModel = vm,
                onBack = { navController.navigateUp() },
                onLogin = { navController.navigate(Screen.Login.route) }
            )
        }

        /* HOME */
        composable(Screen.Home.route) {
            val vm: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = vm,
                onAlbumClick = { album: AlbumInfo ->
                    navController.navigate(Screen.Album.createRoute(album.id))
                }
            )
        }

        /* PROFILE */
        composable(
            route = Screen.Profile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = "1"
            val vm: UserProfileViewModel = hiltViewModel()

            LaunchedEffect(userId) { vm.loadUser(userId) }
            val state = vm.uiState.collectAsState().value

            if (state.user != null) {
                UserProfileScreen(
                    viewModel = vm,
                    user = state.user,
                    reviews = state.reviews,
                    onBackClick = { navController.navigateUp() },
                    onAlbumClick = { album ->
                        navController.navigate(Screen.Album.createRoute(album.id))
                    },
                    onReviewClick = { /* TODO */ },
                    onSettingsClick = { navController.navigate(Screen.Settings.route) },
                    onEditProfile = { navController.navigate(Screen.EditProfile.route) }
                )
            } else {
                SimpleError("Usuario no encontrado")
            }
        }

        /* ALBUM */
        composable(
            route = Screen.Album.route,
            arguments = listOf(navArgument("albumId") { type = NavType.IntType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getInt("albumId")
            val vm: ContentViewModel = hiltViewModel()

            LaunchedEffect(albumId) { vm.setInitial(artistId = null, isOwner = false) }
            val state = vm.uiState.collectAsState().value
            val selectedAlbum = albumId?.let { id -> state.albums.find { it.id == id } }

            if (selectedAlbum != null) {
                AlbumReviewScreen(
                    album = selectedAlbum,
                    onArtistClick = {
                        navController.navigate(Screen.ContentArtist.createRoute(selectedAlbum.artist.id))
                    },
                    onUserClick = { uid ->
                        navController.navigate(Screen.Profile.createRoute(uid.toInt()))
                    }
                )
            } else {
                SimpleError("Álbum no encontrado")
            }
        }

        /* CONTENT ARTIST */
        composable(
            route = Screen.ContentArtist.route,
            arguments = listOf(navArgument("artistId") { type = NavType.IntType })
        ) { backStackEntry ->
            val artistId = backStackEntry.arguments?.getInt("artistId")
            val vm: ContentViewModel = hiltViewModel()
            LaunchedEffect(artistId) { vm.setInitial(artistId = artistId, isOwner = false) }
            ContentScreen(
                viewModel = vm,
                onBack = { navController.navigateUp() },
                onOpenAlbum = { id -> navController.navigate(Screen.Album.createRoute(id)) },
                onSeeAll = { /* TODO */ }
            )
        }

        /* CONTENT USER */
        composable(Screen.ContentUser.route) {
            val vm: ContentViewModel = hiltViewModel()
            LaunchedEffect(Unit) { vm.setInitial(artistId = null, isOwner = true) }
            ContentScreen(
                viewModel = vm,
                onBack = { navController.navigateUp() },
                onOpenAlbum = { id -> navController.navigate(Screen.Album.createRoute(id)) },
                onEditAlbum = { /* TODO */ }
            )
        }

        /* SETTINGS */
        composable(Screen.Settings.route) {
            val vm: SettingsViewModel = hiltViewModel()
            SettingsScreen(
                viewModel = vm,
                onBackClick = { navController.navigateUp() }
            )
        }

        /* EDIT PROFILE */
        composable(Screen.EditProfile.route) {
            val vm: EditProfileViewModel = hiltViewModel()

            // 🧩 Usuario por defecto con id Int
            val defaultUserId = 1

            EditarPerfilScreen(
                viewModel = vm,
                userId = defaultUserId.toString(), // 🔸 EditProfileViewModel espera String
                onBack = { navController.navigateUp() },
                onSaved = {
                    // 🔹 Al guardar, vuelve al perfil del usuario 1 actualizado
                    navController.navigate(Screen.Profile.createRoute(defaultUserId)) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }


        /* ADD REVIEW */
        composable(Screen.AddReview.route) {
            val vm: AddReviewViewModel = hiltViewModel()
            AddReviewScreen(
                viewModel = vm,
                albumList = emptyList(),
                onCancel = { navController.navigateUp() },
                onPublished = { _, _, _, _ -> navController.navigateUp() }
            )
        }
    }
}

/* ---------------------- Utils ---------------------- */
@Composable
private fun SimpleError(message: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppNavHostPreview() {
    val navController = rememberNavController()
    Proyecto_movilTheme {
        Surface { AppNavHost(navController = navController) }
    }
}
