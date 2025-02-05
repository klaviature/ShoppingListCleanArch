package ru.fomin.shoppinglistcleanarch.presentation.shopitem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.fomin.shoppinglistcleanarch.R
import ru.fomin.shoppinglistcleanarch.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {

    companion object {

        private const val EXTRA_SHOP_ITEM_ID = "shop_item_id"

        private const val EXTRA_SCREEN_MODE = "screen_mode"

        private const val SCREEN_MODE_ADD = "screen_mode_add"
        private const val SCREEN_MODE_EDIT = "screen_mode_edit"

        fun newIntentAddSHopItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, SCREEN_MODE_ADD)
            return intent
        }

        fun newIntentEditShopItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, SCREEN_MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }

    private val viewModel by viewModels<ShopItemViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}