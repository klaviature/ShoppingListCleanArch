package ru.fomin.shoppinglistcleanarch.domain

import io.reactivex.rxjava3.core.Completable

class EditShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun editShopItem(shopItem: ShopItem): Completable {
        return shopListRepository.editShopItem(shopItem)
    }
}