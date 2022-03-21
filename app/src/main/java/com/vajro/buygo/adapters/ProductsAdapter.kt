package com.vajro.buygo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vajro.buygo.listeners.OnUpdateListener
import com.vajro.buygo.databinding.AdapterItemProductBinding
import com.vajro.buygo.models.ProductData

class ProductsAdapter(
    val context: Context,
    val products: ArrayList<ProductData>,
    val onUpdateListener: OnUpdateListener?
) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>(), Filterable {

    var productsFiltered: ArrayList<ProductData> = ArrayList()
    var filter = ProductFilter()

    init {
        productsFiltered.clear()
        productsFiltered.addAll(products)
    }

    inner class ViewHolder(val binding: AdapterItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate = LayoutInflater.from(context)
        val binding = AdapterItemProductBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = productsFiltered[position]

        holder.binding.tvProductName.text = model.name
        holder.binding.tvPrice.text = model.special
        holder.binding.tvOldPrice.text = model.price
        holder.binding.tvQty.text = model.qty.toString()

        Glide.with(context)
            .load(model.thumb)
            .into(holder.binding.imgProduct)

        holder.binding.btnAdd.setOnClickListener {
            productsFiltered[holder.adapterPosition].apply {
                this.qty = this.qty + 1
            }
            val item = productsFiltered[holder.adapterPosition]
            holder.binding.tvQty.text = item.qty.toString()
            if (item.qty == 1) {
                onUpdateListener?.onInsert(item)
            } else {
                onUpdateListener?.onUpdate(item.product_id, item.qty)
            }
        }

        holder.binding.btnMinus.setOnClickListener {
            if (productsFiltered[holder.adapterPosition].qty > 0) {
                productsFiltered[holder.adapterPosition].apply {
                    this.qty = this.qty - 1
                }
                val item = productsFiltered[holder.adapterPosition]
                holder.binding.tvQty.text = item.qty.toString()
                onUpdateListener?.onUpdate(item.product_id, item.qty)
            }
        }

    }

    override fun getItemCount(): Int {
        return productsFiltered.size
    }

    inner class ProductFilter : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            productsFiltered.clear()
            productsFiltered.addAll(products.filter { it.name.lowercase().contains(p0.toString()) })
            return FilterResults()
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            notifyDataSetChanged()
        }

    }

    override fun getFilter(): Filter {
        return filter
    }

}