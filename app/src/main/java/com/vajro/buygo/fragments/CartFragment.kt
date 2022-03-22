package com.vajro.buygo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vajro.buygo.listeners.OnUpdateListener
import com.vajro.buygo.Utils
import com.vajro.buygo.adapters.CartItemsAdapter
import com.vajro.buygo.databinding.FragmentCartBinding
import com.vajro.buygo.models.ProductData
import com.vajro.buygo.roomdb.AppDatabase
import com.vajro.buygo.roomdb.Cart
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class CartFragment : Fragment(), OnUpdateListener {

    companion object {
        fun newInstance() = CartFragment()
    }

    private lateinit var binding: FragmentCartBinding
    private var cartItemsAdapter: CartItemsAdapter? = null

    val appDatabase by lazy {
        AppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgHome.setOnClickListener {
            findNavController().popBackStack()
        }

        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.cartDao().getAllCartItems().collect {
                if (cartItemsAdapter == null) {
                    context?.let { context ->
                        cartItemsAdapter =
                            CartItemsAdapter(
                                requireContext(),
                                it as ArrayList<Cart>,
                                this@CartFragment
                            )
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.rvProducts.adapter = cartItemsAdapter
                    }
                }
                CoroutineScope(Dispatchers.Main).launch {
                    binding.imgNoImage.isVisible = it.isEmpty()
                    binding.clTotal.isVisible = it.isNotEmpty()
                    binding.tvItems.text = String.format("Total Items : %d", it.size)
                    val total = it.sumOf { Utils.convertAmount(it.special) * it.quantity }
                    binding.tvTotal.text = String.format("Total : %s", Utils.currencyFormat(total))
                    binding.tvSubTotal.text = Utils.currencyFormat(total, true)
                }
            }
        }

    }

    override fun onUpdate(prodId: Int, qty: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            if (qty > 0) {
                appDatabase.cartDao().update(qty, prodId)
            } else appDatabase.cartDao().delete(prodId)
        }
    }

    override fun onInsert(model: ProductData) {

    }

}