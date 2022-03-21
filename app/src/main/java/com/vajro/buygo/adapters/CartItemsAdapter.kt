package com.vajro.buygo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vajro.buygo.listeners.OnUpdateListener
import com.vajro.buygo.databinding.AdapterItemCartBinding
import com.vajro.buygo.roomdb.Cart

class CartItemsAdapter(
    val context: Context,
    val products: ArrayList<Cart>,
    val onUpdateListener: OnUpdateListener?
) :
    RecyclerView.Adapter<CartItemsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: AdapterItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate = LayoutInflater.from(context)
        val binding = AdapterItemCartBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = products[position]

        holder.binding.tvProductName.text = model.name
        holder.binding.tvPrice.text = model.special
        holder.binding.tvQty.text = model.quantity.toString()

        Glide.with(context)
            .load(model.thumb)
            .into(holder.binding.imgProduct)

        holder.binding.btnAdd.setOnClickListener {
            products[holder.adapterPosition].apply {
                this.quantity = this.quantity + 1
            }
            val item = products[holder.adapterPosition]
            holder.binding.tvQty.text = item.quantity.toString()
            onUpdateListener?.onUpdate(item.product_id, item.quantity)
        }

        holder.binding.btnMinus.setOnClickListener {
            if (products[holder.adapterPosition].quantity > 1) {
                products[holder.adapterPosition].apply {
                    this.quantity = this.quantity - 1
                }
                val item = products[holder.adapterPosition]
                holder.binding.tvQty.text = item.quantity.toString()
                onUpdateListener?.onUpdate(item.product_id, item.quantity)
            } else {
                val item = products[holder.adapterPosition]
                onUpdateListener?.onUpdate(item.product_id, 0)
                products.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
            }
        }

    }

    override fun getItemCount(): Int {
        return products.size
    }

}