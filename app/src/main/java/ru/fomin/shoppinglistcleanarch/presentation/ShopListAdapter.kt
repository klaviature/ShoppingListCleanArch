package ru.fomin.shoppinglistcleanarch.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.fomin.shoppinglistcleanarch.R
import ru.fomin.shoppinglistcleanarch.domain.ShopItem

typealias OnShopItemLongClickListener = (ShopItem) -> Unit

typealias OnShopItemClickListener = (ShopItem) -> Unit

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    companion object {
        const val VIEW_TYPE_ENABLED = 0
        const val VIEW_TYPE_DISABLED = 1

        const val MAX_VIEW_POOL = 20
    }

    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ShopItemViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById<TextView>(R.id.shopItemNameTextView)
        val countTextView: TextView = view.findViewById<TextView>(R.id.shopItemCountTextView)
    }

    var onShopItemLongClickListener: OnShopItemLongClickListener? = null

    var onShopItemClickListener: OnShopItemClickListener? = null

    override fun getItemViewType(position: Int): Int {
        val item = shopList[position]
        return if (item.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    override fun getItemCount(): Int = shopList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
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
        holder.view.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
        holder.view.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
    }
}