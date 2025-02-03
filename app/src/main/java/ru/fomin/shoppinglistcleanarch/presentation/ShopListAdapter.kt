package ru.fomin.shoppinglistcleanarch.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.fomin.shoppinglistcleanarch.R
import ru.fomin.shoppinglistcleanarch.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    companion object {
        const val VIEW_TYPE_ENABLED = 0
        const val VIEW_TYPE_DISABLED = 1

        const val MAX_VIEW_POOL = 20
    }

    inner class ShopItemViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById<TextView>(R.id.shopItemNameTextView)
        val countTextView: TextView = view.findViewById<TextView>(R.id.shopItemCountTextView)
    }

    override fun getItemViewType(position: Int): Int {
        val item = shopList[position]
        return if (item.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var count = 0

    override fun getItemCount(): Int = shopList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d("ShopListAdapter", "Count: ${++count}")
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        val status = if (shopItem.enabled) {
            "Enabled"
        } else {
            "Disabled"
        }
        holder.nameTextView.text = "${shopItem.name} ($status)"
        holder.countTextView.text = String.format(shopItem.count.toString())
        holder.view.setOnLongClickListener {
            true
        }
    }
}