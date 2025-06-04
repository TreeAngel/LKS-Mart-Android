package com.example.lksmartandroid.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.lksmartandroid.R
import com.example.lksmartandroid.activities.InvoiceActivity
import com.example.lksmartandroid.adapters.ProductsAdapter
import com.example.lksmartandroid.databinding.FragmentProductBinding
import com.example.lksmartandroid.models.CheckoutItem
import com.example.lksmartandroid.models.ProductModel
import com.example.lksmartandroid.services.ClientService
import com.example.lksmartandroid.services.ClientService.cart
import com.example.lksmartandroid.services.ClientService.formatPrice
import kotlinx.coroutines.launch

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private lateinit var productsAdapter: ProductsAdapter

    private var products: ArrayList<ProductModel> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setupAdapter()
            inputSearch.addTextChangedListener {
                if (it.toString().isEmpty()) {
                    getProducts("")
                } else {
                    getProducts(it.toString())
                }
            }
            btnCheckout.setOnClickListener {
                if (cart.isEmpty()) {
                    Toast.makeText(context, "Cart is empty!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                checkout()
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            if (products.isEmpty()) {
                getProducts("")
            }
            updateSubtotal()
        }
    }

    private fun checkout() {
        try {
            lifecycleScope.launch {
                with(ClientService) {
                    val response = service.checkout(request = cart)
                    if (response.isSuccessful) {
                        startActivity(
                            Intent(requireContext(), InvoiceActivity::class.java)
                                .putExtra("Cart", cart)
                        )
                        cart.clear()
                        updateSubtotal()
                    } else {
                        Toast.makeText(
                            context,
                            "Checkout Failed\n${response.body()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(
                            "LKS MART API ERROR",
                            "checkout: ${response.message()} | ${response.errorBody()}"
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun setupAdapter() = binding.rvProducts.apply {
        productsAdapter = ProductsAdapter { item, qty ->
            with(ClientService) {
                val exist = cart.find { it.productId == item.id && it.qty == qty }
                if (exist != null) {
                    exist.qty = qty
//                    exist.subtotal = exist.price * qty
                } else {
                    cart.add(CheckoutItem(item.id, qty, item.nama, item.hargaSatuan))
                }
                updateSubtotal()
            }
        }
        adapter = productsAdapter
        productsAdapter.products = products
        getProducts("")
    }

    private fun getProducts(search: String?) {
        try {
            lifecycleScope.launch {
                with(ClientService) {
                    val response = service.products(search)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            productsAdapter.products = it
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to retrieve data\n${response.message()}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        Log.d(
                            "LKS MART API ERROR",
                            "getProducts: ${response.message()} | ${response.errorBody()}"
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun updateSubtotal() {
        binding.tvSubtotal.text = formatPrice(cart.sumOf { it.subtotal })
    }
}