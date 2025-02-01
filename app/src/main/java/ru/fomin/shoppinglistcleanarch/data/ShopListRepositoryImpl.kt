package ru.fomin.shoppinglistcleanarch.data

import ru.fomin.shoppinglistcleanarch.domain.ShopItem
import ru.fomin.shoppinglistcleanarch.domain.ShopListRepository

object ShopListRepositoryImpl: ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()

    private var autoIncrementId= 0

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
    }

    override fun getShopList(): List<ShopItem> {
        return shopList.toList()
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Element with ID: $shopItemId was not found!")
    }

    override fun editShopItem(shopItem: ShopItem) {
        val shopItemIndex = shopList.indexOf(shopItem)
        shopList[shopItemIndex] = shopItem
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }
}