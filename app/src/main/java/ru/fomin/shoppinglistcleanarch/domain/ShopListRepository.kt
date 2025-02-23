package ru.fomin.shoppinglistcleanarch.domain

import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface ShopListRepository {

    fun getShopList(): LiveData<List<ShopItem>>

    fun getShopItem(shopItemId: Int): Single<ShopItem>

    fun addShopItem(shopItem: ShopItem): Completable

    fun editShopItem(shopItem: ShopItem): Completable

    fun deleteShopItem(shopItem: ShopItem): Completable
}