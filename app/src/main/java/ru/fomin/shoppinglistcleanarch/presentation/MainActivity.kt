package ru.fomin.shoppinglistcleanarch.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.fomin.shoppinglistcleanarch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var shopListAdapter: ShopListAdapter

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

        setupAdapter()
        viewModel.shopListLiveData.observe(this) { shopList ->
            Log.d(TAG, shopList.toString())
            shopListAdapter.shopList = shopList
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
                    val shopItem = shopListAdapter.shopList[viewHolder.adapterPosition]
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
            Log.d("MainActivity", shopItem.toString())
        }
    }
}