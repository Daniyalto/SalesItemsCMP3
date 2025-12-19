package com.example.salesapp.repository

import com.example.salesapp.model.SaleItem

// Interface
interface ItemsRepository {
    suspend fun getSalesItems(): Result<List<SaleItem>>
    suspend fun add(item: SaleItem): Result<SaleItem>
    suspend fun delete(id: Int): Result<Unit>
}

// Implementering
class ItemsRepositoryImpl(private val itemService: ItemService) : ItemsRepository {

    override suspend fun getSalesItems(): Result<List<SaleItem>> = runCatching {
        itemService.getAllItems()
    }

    override suspend fun add(item: SaleItem): Result<SaleItem> = runCatching {
        itemService.addItem(item)
    }

    override suspend fun delete(id: Int): Result<Unit> = runCatching {
        itemService.deleteItem(id)
    }
}

