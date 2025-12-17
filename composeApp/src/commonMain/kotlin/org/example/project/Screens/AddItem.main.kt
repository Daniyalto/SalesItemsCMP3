package com.example.salesapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.salesapp.model.SaleItem
import io.ktor.util.date.getTimeMillis
import org.example.project.rememberNotificationManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    currentUserEmail: String,
    onSave: (SaleItem) -> Unit,
    onCancel: () -> Unit
) {
    // Notification Manager
    val notificationManager = rememberNotificationManager()

    // Request notification permission når skærmen åbnes
    LaunchedEffect(Unit) {
        notificationManager.requestPermission { granted ->
            println("Notification permission: $granted")
        }
    }

    // UI State for input fields
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var sellerEmail by remember { mutableStateOf(currentUserEmail) }
    var sellerPhone by remember { mutableStateOf("") }
    var pictureUrl by remember { mutableStateOf("") }

    // Validation State
    var descriptionError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Item") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Cancel")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it; descriptionError = false },
                label = { Text("Description *") },
                modifier = Modifier.fillMaxWidth(),
                isError = descriptionError,
                supportingText = { if (descriptionError) Text("Description is required") },
                maxLines = 3
            )

            // Price Field
            OutlinedTextField(
                value = price,
                onValueChange = { price = it.filter { char -> char.isDigit() }; priceError = false },
                label = { Text("Price (DKK) *") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = priceError,
                supportingText = { if (priceError) Text("Valid price is required") },
                singleLine = true
            )

            Text(text = "Contact Information", style = MaterialTheme.typography.titleMedium)

            // Email Field
            OutlinedTextField(
                value = sellerEmail,
                onValueChange = { sellerEmail = it; emailError = false },
                label = { Text("Email *") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError,
                supportingText = { if (emailError) Text("Valid email is required") },
                singleLine = true
            )

            // Phone Field
            OutlinedTextField(
                value = sellerPhone,
                onValueChange = { sellerPhone = it.filter { char -> char.isDigit() }; phoneError = false },
                label = { Text("Phone *") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = phoneError,
                supportingText = { if (phoneError) Text("Phone number is required") },
                singleLine = true
            )

            // Picture URL
            OutlinedTextField(
                value = pictureUrl,
                onValueChange = { pictureUrl = it },
                label = { Text("Picture URL (optional)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                singleLine = true
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        var hasError = false

                        // Validation Logic
                        if (description.isBlank()) { descriptionError = true; hasError = true }

                        val priceValue = price.toIntOrNull()
                        if (priceValue == null || priceValue <= 0) { priceError = true; hasError = true }

                        if (sellerEmail.isBlank() || !sellerEmail.contains("@") || !sellerEmail.contains(".")) {
                            emailError = true
                            hasError = true
                        }

                        if (sellerPhone.isBlank() || sellerPhone.length < 5) { phoneError = true; hasError = true }

                        if (!hasError) {
                            val newItem = SaleItem(
                                id = 0,
                                description = description.trim(),
                                price = priceValue!!,
                                sellerEmail = sellerEmail.trim(),
                                sellerPhone = sellerPhone.trim(),
                                time = getTimeMillis(),
                                pictureUrl = pictureUrl.trim()
                            )

                            // Vis notification når item er tilføjet!
                            notificationManager.showNotification(
                                title = "Item Added!",
                                message = "${description.trim()} - $priceValue DKK"
                            )

                            onSave(newItem)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save Item")
                }
            }
        }
    }
}