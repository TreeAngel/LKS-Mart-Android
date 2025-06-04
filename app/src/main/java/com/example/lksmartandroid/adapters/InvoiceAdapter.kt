package com.example.lksmartandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lksmartandroid.databinding.ItemInvoiceBinding
import com.example.lksmartandroid.models.CheckoutItem
import com.example.lksmartandroid.services.ClientService

class InvoiceAdapter : RecyclerView.Adapter<InvoiceAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemInvoiceBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<CheckoutItem>() {
        override fun areItemsTheSame(oldItem: CheckoutItem, newItem: CheckoutItem): Boolean {
            return oldItem.productId == newItem.productId && oldItem.qty == newItem.qty
        }

        override fun areContentsTheSame(oldItem: CheckoutItem, newItem: CheckoutItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this@InvoiceAdapter, diffUtil)
    var cart: List<CheckoutItem>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = cart.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val item = cart[position]
            tvName.text = item.name
            tvPrice.text = ClientService.formatPrice(item.price)
            tvQty.text = "${item.qty}"
            tvSubtotal.text = ClientService.formatPrice(item.subtotal)
        }
    }
}