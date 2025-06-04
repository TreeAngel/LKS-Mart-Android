package com.example.lksmartandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lksmartandroid.databinding.ItemProductsBinding
import com.example.lksmartandroid.models.ProductModel
import com.example.lksmartandroid.services.ClientService

class ProductsAdapter(val onAddToCart: (ProductModel, Int) -> Unit) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemProductsBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<ProductModel>() {
        override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtil)
    var products: List<ProductModel>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemProductsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val product = products[position]
            ClientService.loadImage(root.context, ivProduct, product.image)
            tvName.text = product.nama
            tvPrice.text = ClientService.formatPrice(product.hargaSatuan)
            tvQty.text = "0"
            tvRating.text = "4.5"
            ibAdd.setOnClickListener {
                var qty = tvQty.text.toString().toInt()
                if (qty < product.jumlah) {
                    qty += 1
                    tvQty.text = "$qty"
                }
            }
            ibRemove.setOnClickListener {
                var qty = tvQty.text.toString().toInt()
                if (qty > 0) {
                    qty -= 1
                    tvQty.text = "$qty"
                }
            }
            ibAddToCart.setOnClickListener {
                val qty = tvQty.text.toString().toInt()
                if (qty > 0) {
                    onAddToCart(product, qty)
                }
            }
        }
    }
}