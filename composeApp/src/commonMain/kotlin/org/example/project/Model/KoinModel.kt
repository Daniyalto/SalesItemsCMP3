package org.example.project.Model

import com.example.salesapp.model.AuthViewModel
import com.example.salesapp.repository.ItemService
import com.example.salesapp.repository.ItemsRepository
import com.example.salesapp.repository.ItemsRepositoryImpl
import org.koin.dsl.module
import org.koin.core.context.startKoin

val appModule = module {
    
    single { ItemService() }
    single<ItemsRepository> { ItemsRepositoryImpl(itemService = get()) }

    
    factory { SaleItemViewModel(repository = get()) }
    factory { AuthViewModel() }
}


fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
