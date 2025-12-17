package com.example.salesapp.repository

import com.example.salesapp.model.SaleItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val BASE_URL = "https://anbo-salesitems.azurewebsites.net/api/SalesItems"

class ItemService {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun getAllItems(): List<SaleItem> =
        client.get(BASE_URL).body()

    suspend fun addItem(item: SaleItem): SaleItem =
        client.post(BASE_URL) {
            contentType(ContentType.Application.Json)
            setBody(item)
        }.body()

    suspend fun deleteItem(id: Int) {
        client.delete("$BASE_URL/$id")
    }
}

