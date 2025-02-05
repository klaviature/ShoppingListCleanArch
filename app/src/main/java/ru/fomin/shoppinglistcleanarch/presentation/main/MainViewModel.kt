package ru.fomin.shoppinglistcleanarch.presentation.main

import androidx.lifecycle.ViewModel
import ru.fomin.shoppinglistcleanarch.data.ShopListRepositoryImpl
import ru.fomin.shoppinglistcleanarch.domain.DeleteShopItemUseCase
import ru.fomin.shoppinglistcleanarch.domain.EditShopItemUseCase
import ru.fomin.shoppinglistcleanarch.domain.GetShopListUseCase
import ru.fomin.shoppinglistcleanarch.domain.ShopItem

class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)

    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)

    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopListLiveData = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun toggleShopItemEnabled(shopItem: ShopItem) {
        val toggledShopItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(toggledShopItem)
    }
}