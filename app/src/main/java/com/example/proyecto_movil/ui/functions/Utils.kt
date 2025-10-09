package com.example.proyecto_movil.ui.functions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object Utils {

    fun getCurrentUserPhoto(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        val photoUrl: String = currentUser?.photoUrl?.toString() ?: ""
        return photoUrl
    }

    fun getCurrentUserId(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        val photoUrl: String = currentUser?.uid ?: ""
        return photoUrl
    }

    fun getCurrentUserName(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        val photoUrl: String = currentUser?.displayName ?: ""
        return photoUrl
    }

    fun getCurrentUserEmail(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        val photoUrl: String = currentUser?.email ?: ""
        return photoUrl
    }

    fun getCurrentUser(): FirebaseUser? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser
    }

    @Composable
    fun getCurrentRoute(navController: NavHostController): String {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: ""
        return currentRoute
    }

}
