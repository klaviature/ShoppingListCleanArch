package ru.fomin.shoppinglistcleanarch.domain

import io.reactivex.rxjava3.core.Single

class GetShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun getShopItem(shopItemId: Int): Single<ShopItem> {
        return shopListRepository.getShopItem(shopItemId)
    }
}