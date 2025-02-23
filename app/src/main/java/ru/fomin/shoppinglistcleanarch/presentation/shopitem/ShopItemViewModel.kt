package ru.fomin.shoppinglistcleanarch.presentation.shopitem

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.fomin.shoppinglistcleanarch.data.ShopListRepositoryImpl
import ru.fomin.shoppinglistcleanarch.domain.AddShopItemUseCase
import ru.fomin.shoppinglistcleanarch.domain.EditShopItemUseCase
import ru.fomin.shoppinglistcleanarch.domain.GetShopItemUseCase
import ru.fomin.shoppinglistcleanarch.domain.ShopItem

class ShopItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    private val _shopItemLiveData = MutableLiveData<ShopItem>()
    val shopItemLiveData: LiveData<ShopItem> get() = _shopItemLiveData

    private val _nameFieldErrorLiveData = MutableLiveData(false)
    val nameFieldErrorLiveData: LiveData<Boolean> get() = _nameFieldErrorLiveData

    private val _countFieldErrorLiveData = MutableLiveData(false)
    val countFieldErrorLiveData: LiveData<Boolean> get() = _countFieldErrorLiveData

    private val _shouldCloseScreenLiveData = MutableLiveData<Unit>()
    val shouldCloseScreenLiveData: LiveData<Unit> = _shouldCloseScreenLiveData

    private val compositeDisposable = CompositeDisposable()

    fun getShopItem(id: Int) {
        val disposable = getShopItemUseCase.getShopItem(id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                _shopItemLiveData.postValue(it)
            }
            .doOnError {
                Log.d("ShopItemViewModel", it.message.toString())
            }
            .subscribe()
        compositeDisposable.add(disposable)
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val validateResult = validateInput(name, count)
        if (validateResult) {
            val shopItem = ShopItem(name, count, true)
            val disposable = addShopItemUseCase.addShopItem(shopItem)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    _shouldCloseScreenLiveData.postValue(Unit)
                }
                .doOnError {
                    Log.d("ShopItemViewModel", it.message.toString())
                }
                .subscribe()
            compositeDisposable.add(disposable)
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val validateResult = validateInput(name, count)
        if (validateResult) {
            _shopItemLiveData.value?.let {
                val shopItem = it.copy(name = name, count = count)
                val disposable = editShopItemUseCase.editShopItem(shopItem)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        _shouldCloseScreenLiveData.postValue(Unit)
                    }
                    .subscribe()
                compositeDisposable.add(disposable)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun clearNameFieldError() {
        _nameFieldErrorLiveData.value = false
    }

    fun clearCountFieldError() {
        _countFieldErrorLiveData.value = false
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return inputCount?.trim()?.toIntOrNull() ?: 0
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var isValid = true
        if (name.isBlank()) {
            _nameFieldErrorLiveData.postValue(true)
            isValid = false
        }
        if (count <= 0) {
            _countFieldErrorLiveData.postValue(true)
            isValid = false
        }
        return isValid
    }
}