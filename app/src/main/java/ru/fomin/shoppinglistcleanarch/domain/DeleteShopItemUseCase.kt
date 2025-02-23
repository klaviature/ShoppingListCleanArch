package ru.fomin.shoppinglistcleanarch.domain

import io.reactivex.rxjava3.core.Completable

class DeleteShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun deleteShopItem(shopItem: ShopItem): Completable {
        return shopListRepository.deleteShopItem(shopItem)
    }
}