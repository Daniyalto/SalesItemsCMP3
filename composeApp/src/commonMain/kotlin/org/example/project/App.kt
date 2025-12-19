package com.example.salesapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.salesapp.screens.LoginScreen
import com.example.salesapp.screens.AddItemScreen
import com.example.salesapp.screens.ItemDetailsScreen
import com.example.salesapp.model.AuthViewModel
import org.example.project.Model.SaleItemViewModel
import org.koin.compose.koinInject
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.project.Screens.SalesItemsScreen
import androidx.navigation.navArgument
import androidx.navigation.NavType


private val AppColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC5)
)

@Composable
fun App() {

    val salesViewModel: SaleItemViewModel = koinInject()
    val authViewModel: AuthViewModel = koinInject()

    val navController = rememberNavController()

    val salesState by salesViewModel.state.collectAsState()
    val authState by authViewModel.state.collectAsState()

    MaterialTheme(colorScheme = AppColorScheme) {
        Surface(modifier = Modifier.fillMaxSize()) {

            NavHost(
                navController = navController,
                startDestination = NavRoutes.SalesItems.route
            ) {

                // Login
                composable(NavRoutes.Register.route) {
                    LoginScreen(
                        onLoginSuccess = {
                            navController.navigate(NavRoutes.SalesItems.route) {
                                // Rydder login/register skærmen fra back stack
                                popUpTo(NavRoutes.Register.route) { inclusive = true }
                            }
                        }
                    )
                }

                // Liste af sales items
                composable(NavRoutes.SalesItems.route) {
                    SalesItemsScreen(
                        items = salesState.items,
                        errorMessage = authState.errorMessage ?: salesState.errorMessage.orEmpty(),
                        isLoggedIn = authState.isLoggedIn, // Status er styret af AuthViewModel
                        currentUserEmail = authState.currentUserEmail,
                        itemsLoading = salesState.isLoading,

                        onItemSelected = { item ->
                            navController.navigate(NavRoutes.ItemDetails.createRoute(item.id))
                        },

                        onItemDelete = { item -> salesViewModel.delete(item) },

                        onItemAdd = {
                            navController.navigate(NavRoutes.AddItem.route)
                        },

                        onLoginClick = {
                            navController.navigate(NavRoutes.Register.route)
                        },

                        onLogoutClick = {
                            authViewModel.logout() // Kalder logout i AuthViewModel
                        },

                        sortByPrice = salesViewModel::sortItemByPrice,
                        sortByDateTime = salesViewModel::sortItemByDateTime,
                        filterByDescription = salesViewModel::filterItemByDescription,
                        filterByPrice = salesViewModel::filterItemByPrice
                    )
                }
                composable(
                    route = NavRoutes.ItemDetails.route + "/{itemId}",
                    arguments = listOf(
                        navArgument("itemId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    
                    val itemId = backStackEntry.arguments?.getString("itemId")?.toIntOrNull()

                    val item = salesState.items.find { it.id == itemId } // Sammenlign to Int?

                    if (item != null) {
                        ItemDetailsScreen(
                            item = item,
                            onBack = { navController.navigateUp() }
                        )
                    } else {
                        LaunchedEffect(Unit) {
                            navController.navigateUp()
                        }
                    }
                }

                // Add item
                composable(NavRoutes.AddItem.route) {
                    AddItemScreen(
                        currentUserEmail = authState.currentUserEmail,
                        onSave = { newItem ->
                            salesViewModel.add(newItem)
                            navController.navigateUp()
                        },
                        onCancel = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}
