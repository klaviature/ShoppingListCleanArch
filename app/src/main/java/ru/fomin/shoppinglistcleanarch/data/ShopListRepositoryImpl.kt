package ru.fomin.shoppinglistcleanarch.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.fomin.shoppinglistcleanarch.domain.ShopItem
import ru.fomin.shoppinglistcleanarch.domain.ShopListRepository
import kotlin.random.Random

object ShopListRepositoryImpl: ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()

    private val shopListLiveData = MutableLiveData<List<ShopItem>>()

    init {
        val items = mutableListOf<ShopItem>()
        for (i in 1..100) {
            items.add(
                ShopItem("Name $i", i, Random.nextBoolean())
            )
        }
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