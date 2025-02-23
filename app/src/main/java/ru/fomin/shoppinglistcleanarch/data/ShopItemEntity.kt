package ru.fomin.shoppinglistcleanarch.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_items")
data class ShopItemEntity(
    val name: String,
    val count: Int,
    val enabled: Boolean,
    @PrimaryKey(autoGenerate = true) var id: Int?
)