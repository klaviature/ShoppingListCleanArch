package ru.fomin.shoppinglistcleanarch.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_items")
data class ShopItem(
    val name: String,
    val count: Int,
    val enabled: Boolean,
    @PrimaryKey var id: Int = UNDEFINED_ID
) {

    companion object {

        const val UNDEFINED_ID = -1
    }
}
