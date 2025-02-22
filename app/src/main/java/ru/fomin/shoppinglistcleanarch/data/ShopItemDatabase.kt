package ru.fomin.shoppinglistcleanarch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShopItemEntity::class], version = 1)
abstract class ShopItemDatabase : RoomDatabase() {

    abstract fun shopItemDao(): ShopItemDao

    companion object {

        private var _instance: ShopItemDatabase? = null

        fun getInstance(context: Context): ShopItemDatabase {
            return _instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShopItemDatabase::class.java,
                    "shop_item_database"
                ).build()
                _instance = instance
                instance
            }
        }
    }
}