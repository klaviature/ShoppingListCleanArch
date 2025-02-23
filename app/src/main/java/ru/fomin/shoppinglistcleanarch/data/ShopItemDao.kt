package ru.fomin.shoppinglistcleanarch.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface ShopItemDao {

    @Query("SELECT * FROM shop_items")
    fun getAll(): LiveData<List<ShopItemEntity>>

    @Query("SELECT * FROM shop_items WHERE id LIKE :id")
    fun getShopItem(id: Int): Single<ShopItemEntity>

    @Insert
    fun addShopItem(shopItem: ShopItemEntity): Completable

    @Delete
    fun deleteShopItem(shopItem: ShopItemEntity): Completable

    @Update
    fun updateShopItem(shopItem: ShopItemEntity): Completable
}