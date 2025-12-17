package org.example.project.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.salesapp.model.SaleItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesItemsScreen(
    items: List<SaleItem>,
    errorMessage: String,
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean = false,
    currentUserEmail: String = "",
    onItemSelected: (SaleItem) -> Unit = {},
    onItemDelete: (SaleItem) -> Unit = {},
    onLoginClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    itemsLoading: Boolean = false,
    onItemAdd: () -> Unit = {},
    sortByPrice: (Boolean) -> Unit = {},
    sortByDateTime: (Boolean) -> Unit = {},
    filterByDescription: (String) -> Unit = {},
    filterByPrice: (Int?) -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Sales Items") },
                actions = {
                    if (isLoggedIn) {
                        // Viser LOG UD knappen
                        IconButton(onClick = onLogoutClick) {
                            Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
                        }
                    } else {
                        // Viser LOGIN knappen
                        TextButton(onClick = onLoginClick) {
                            Text("Login")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            // VIGTIGT: Vises kun hvis brugeren er logget ind
            if (isLoggedIn) {
                FloatingActionButton(onClick = onItemAdd) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Item")
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp) // Anvender padding til indholdet
        ) {
            // Error message
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (itemsLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@Scaffold
            }

            if (items.isEmpty() && !itemsLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No items found")
                }
                return@Scaffold
            }

            // Filter states
            var searchText by rememberSaveable { mutableStateOf("") }
            var priceText by rememberSaveable { mutableStateOf("") }

            // Search + filter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        filterByDescription(it.trim())
                    },
                    label = { Text("Search") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = priceText,
                    onValueChange = {
                        priceText = it
                        filterByPrice(it.toIntOrNull())
                    },
                    label = { Text("Max price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.width(150.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            // Sorting chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(onClick = { sortByPrice(true) }, label = { Text("Price ↑") })
                AssistChip(onClick = { sortByPrice(false) }, label = { Text("Price ↓") })
                AssistChip(onClick = { sortByDateTime(true) }, label = { Text("Date ↑") })
                AssistChip(onClick = { sortByDateTime(false) }, label = { Text("Date ↓") })
            }

            Spacer(Modifier.height(12.dp))

            // List
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
            ) {
                items(items) { item ->
                    val isOwner = isLoggedIn && item.sellerEmail == currentUserEmail

                    // **Slettet SwipeDeleteContainer her**
                    SalesItemRowContent(
                        item = item,
                        onItemClick = { onItemSelected(item) },
                        showTrailingDelete = isOwner,
                        onDeleteClick = { onItemDelete(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun SalesItemRowContent(
    item: SaleItem,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit,
    showTrailingDelete: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onItemClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.description, style = MaterialTheme.typography.titleMedium)
                Text("Price: ${item.price} DKK")
                Text("Contact: ${item.sellerEmail}")
            }

            if (showTrailingDelete) {
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

// **SwipeDeleteContainer er fjernet fra filen.**