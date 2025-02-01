package ru.fomin.shoppinglistcleanarch.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _shopListLiveData = MutableLiveData<List<ShopItem>>()
    val shopListLiveData: LiveData<List<ShopItem>> get() = _shopListLiveData

    fun getShopList() {
        val shopList = getShopListUseCase.getShopList()
        _shopListLiveData.postValue(shopList)
    }

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
        getShopList()
    }

    fun toggleShopItemEnabled(shopItem: ShopItem) {
        val toggledShopItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(toggledShopItem)
        getShopList()
    }
}