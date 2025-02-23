package ru.fomin.shoppinglistcleanarch.presentation.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.fomin.shoppinglistcleanarch.data.ShopListRepositoryImpl
import ru.fomin.shoppinglistcleanarch.domain.DeleteShopItemUseCase
import ru.fomin.shoppinglistcleanarch.domain.EditShopItemUseCase
import ru.fomin.shoppinglistcleanarch.domain.GetShopListUseCase
import ru.fomin.shoppinglistcleanarch.domain.ShopItem

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application.applicationContext)

    private val getShopListUseCase = GetShopListUseCase(repository)

    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)

    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopListLiveData = getShopListUseCase.getShopList()

    private val compositeDisposable = CompositeDisposable()

    fun deleteShopItem(shopItem: ShopItem) {
        val disposable = deleteShopItemUseCase.deleteShopItem(shopItem)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {

            }
            .subscribe()
        compositeDisposable.add(disposable)
    }

    fun toggleShopItemEnabled(shopItem: ShopItem) {
        val toggledShopItem = shopItem.copy(enabled = !shopItem.enabled)
        val disposable = editShopItemUseCase.editShopItem(toggledShopItem)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}