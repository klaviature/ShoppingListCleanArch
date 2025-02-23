package ru.fomin.shoppinglistcleanarch.domain

import io.reactivex.rxjava3.core.Completable

class AddShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun addShopItem(shopItem: ShopItem): Completable {
        return shopListRepository.addShopItem(shopItem)
    }
}