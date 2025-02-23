package ru.fomin.shoppinglistcleanarch.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.fomin.shoppinglistcleanarch.domain.ShopItem
import ru.fomin.shoppinglistcleanarch.domain.ShopListRepository

class ShopListRepositoryImpl(context: Context) : ShopListRepository {

    private val shopItemDao = ShopItemDatabase.getInstance(context).shopItemDao()

    override fun addShopItem(shopItem: ShopItem): Completable {
        return shopItemDao.addShopItem(shopItem.toShopItemEntity())
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopItemDao.getAll().map { shopItemEntities ->
            shopItemEntities.map { it.toShopItem() }
        }
    }

    override fun getShopItem(shopItemId: Int): Single<ShopItem> {
        return shopItemDao.getShopItem(shopItemId).map { it.toShopItem() }
    }

    override fun editShopItem(shopItem: ShopItem): Completable {
        return shopItemDao.updateShopItem(shopItem.toShopItemEntity())
    }

    override fun deleteShopItem(shopItem: ShopItem): Completable {
        return shopItemDao.deleteShopItem(shopItem.toShopItemEntity())
    }

    private fun ShopItem.toShopItemEntity(): ShopItemEntity {
        return ShopItemEntity(
            name = this.name,
            count = this.count,
            enabled = this.enabled,
            id = if (this.id != ShopItem.UNDEFINED_ID) this.id else null
        )
    }

    private fun ShopItemEntity.toShopItem(): ShopItem {
        return ShopItem(
            name = this.name,
            count = this.count,
            enabled = this.enabled,
            id = this.id ?: ShopItem.UNDEFINED_ID
        )
    }
}