package ru.fomin.shoppinglistcleanarch.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.fomin.shoppinglistcleanarch.R
import ru.fomin.shoppinglistcleanarch.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    inner class ShopItemViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById<TextView>(R.id.shopItemNameTextView)
        val countTextView: TextView = view.findViewById<TextView>(R.id.shopItemCountTextView)
    }

    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = shopList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_shop_enabled,
            parent,
            false
        )
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