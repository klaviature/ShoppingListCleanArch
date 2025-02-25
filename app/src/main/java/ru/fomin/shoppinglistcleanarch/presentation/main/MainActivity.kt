package ru.fomin.shoppinglistcleanarch.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.fomin.shoppinglistcleanarch.R
import ru.fomin.shoppinglistcleanarch.databinding.ActivityMainBinding
import ru.fomin.shoppinglistcleanarch.presentation.shopitem.ShopItemActivity
import ru.fomin.shoppinglistcleanarch.presentation.shopitem.ShopItemFragment

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var shopListAdapter: ShopListAdapter

    private var shopItemContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        shopItemContainer = binding.fragmentShopItemContainer

        setupAdapter()
        viewModel.shopList.observe(this) { shopList ->
            Log.d(TAG, shopList.toString())
            shopListAdapter.submitList(shopList)
        }

        binding.addShopItemButton.setOnClickListener {
            if (isPortraitOrientation()) {
                val intent = ShopItemActivity.newIntentAddSHopItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddShopItem())
            }
        }
    }

    private fun setupAdapter() {
        shopListAdapter = ShopListAdapter()
        with(binding.shopListRecyclerView) {
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_VIEW_POOL
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_VIEW_POOL
            )
        }
        setupShopItemClickListener()
        setupShopItemLongClickListener()
        setupSwipeListener()
    }

    private fun setupSwipeListener() {
        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val shopItem = shopListAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.deleteShopItem(shopItem)
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.shopListRecyclerView)
    }

    private fun setupShopItemLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = { shopItem ->
            viewModel.toggleShopItemEnabled(shopItem)
        }
    }

    private fun setupShopItemClickListener() {
        shopListAdapter.onShopItemClickListener = { shopItem ->
            if (isPortraitOrientation()) {
                val intent = ShopItemActivity.newIntentEditShopItem(this, shopItem.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceEditShopItem(shopItem.id))
            }
        }
    }

    private fun isPortraitOrientation(): Boolean {
        return shopItemContainer == null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_shop_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}