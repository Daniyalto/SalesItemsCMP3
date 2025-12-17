package org.example.project.Model

import com.example.salesapp.model.AuthViewModel
import com.example.salesapp.repository.ItemService
import com.example.salesapp.repository.ItemsRepository
import com.example.salesapp.repository.ItemsRepositoryImpl
import org.koin.dsl.module
import org.koin.core.context.startKoin

val appModule = module {
    // Singletons
    single { ItemService() }
    single<ItemsRepository> { ItemsRepositoryImpl(itemService = get()) }

    // Factories (View Models)
    factory { SaleItemViewModel(repository = get()) }
    factory { AuthViewModel() }
}

// Global funktion til at starte Koin i native code (kaldes fra MainActivity.kt / iOS Swift)
fun initKoin() {
    startKoin {
        modules(appModule)
    }
}