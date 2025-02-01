package ru.fomin.shoppinglistcleanarch.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.fomin.shoppinglistcleanarch.domain.ShopItem
import ru.fomin.shoppinglistcleanarch.domain.ShopListRepository

object ShopListRepositoryImpl: ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()

    private val shopListLiveData = MutableLiveData<List<ShopItem>>()

    init {
        val items = listOf(
            ShopItem(name = "Tomatoes", count = 6, enabled = true),
            ShopItem(name = "Cucumbers", count = 2, enabled = true),
            ShopItem(name = "Bread", count = 1, enabled = false),
            ShopItem(name = "Mayonnaise", count = 1, enabled = true)
        )
        items.forEach { shopItem ->
            addShopItem(shopItem)
        }
    }

    private var autoIncrementId = 0

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        updateLiveData()
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLiveData
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Element with ID: $shopItemId was not found!")
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldItem = getShopItem(shopItem.id)
        val shopItemIndex = shopList.indexOf(oldItem)
        shopList[shopItemIndex] = shopItem
        updateLiveData()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateLiveData()
    }

    private fun updateLiveData() {
        shopListLiveData.postValue(shopList.toList())
    }
}