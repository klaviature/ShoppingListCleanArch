package ru.fomin.shoppinglistcleanarch.presentation.shopitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.fomin.shoppinglistcleanarch.data.ShopListRepositoryImpl
import ru.fomin.shoppinglistcleanarch.domain.AddShopItemUseCase
import ru.fomin.shoppinglistcleanarch.domain.EditShopItemUseCase
import ru.fomin.shoppinglistcleanarch.domain.GetShopItemUseCase
import ru.fomin.shoppinglistcleanarch.domain.ShopItem

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

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

    fun getShopItem(id: Int) {
        val shopItem =  getShopItemUseCase.getShopItem(id)
        _shopItemLiveData.postValue(shopItem)
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val validateResult = validateInput(name, count)
        if (validateResult) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
            _shouldCloseScreenLiveData.postValue(Unit)
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val validateResult = validateInput(name, count)
        if (validateResult) {
            _shopItemLiveData.value?.let {
                val shopItem = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(shopItem)
                _shouldCloseScreenLiveData.postValue(Unit)
            }
        }
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