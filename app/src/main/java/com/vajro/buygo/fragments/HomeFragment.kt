package com.vajro.buygo.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vajro.buygo.listeners.OnUpdateListener
import com.vajro.buygo.R
import com.vajro.buygo.Utils
import com.vajro.buygo.adapters.ProductsAdapter
import com.vajro.buygo.databinding.FragmentHomeBinding
import com.vajro.buygo.models.ProductData
import com.vajro.buygo.network.Status
import com.vajro.buygo.roomdb.AppDatabase
import com.vajro.buygo.roomdb.Cart
import com.vajro.buygo.viewmodels.HomeViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment(), OnUpdateListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private var productsAdapter: ProductsAdapter? = null

    var products = ArrayList<ProductData>()

    val appDatabase by lazy {
        AppDatabase.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.productsResponse.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressBar.isVisible = false
                    it.data?.products?.let {
                        products = it
                        checkCartProducts()
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.isVisible = false
                    binding.llNoInternet.isVisible = true
                }
                Status.LOADING -> {
                    binding.progressBar.isVisible = true
                    binding.llNoInternet.isVisible = false
                }
            }
        })

        if (productsAdapter == null) {
            viewModel.getProducts()
        } else {
            productsAdapter = null
            checkCartProducts()
        }

        binding.btnCheckout.setOnClickListener {
            binding.etSearch.setText("")
            findNavController().navigate(R.id.CartFragment)
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            private var searchFor = ""
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText = p0.toString().trim()
                if (searchText == searchFor) return
                searchFor = searchText
                CoroutineScope(Dispatchers.Main).launch {
                    delay(300)
                    if (searchText != searchFor) return@launch
                    productsAdapter?.getFilter()?.filter(p0.toString().lowercase())
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.layoutNointernet.tvReload.setOnClickListener {
            viewModel.getProducts()
        }

    }

    fun checkCartProducts() {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.cartDao().getAllCartItems().collect {
                if (productsAdapter == null) {
                    products.map { prod ->
                        prod.qty = 0
                        it.find { it.product_id == prod.product_id }?.let {
                            prod.qty = it.quantity
                        }
                    }
                    displayProducts()
                }
                CoroutineScope(Dispatchers.Main).launch {
                    binding.clTotal.isVisible = it.size > 0
                    binding.tvItems.text = String.format("%d Items", it.size)
                    val total = it.sumOf { Utils.convertAmount(it.special) * it.quantity }
                    binding.tvTotal.text = String.format("Total : %s", Utils.currencyFormat(total))
                }
            }
        }
    }

    fun displayProducts() {
        CoroutineScope(Dispatchers.Main).launch {
            context?.let {
                productsAdapter = ProductsAdapter(it, products, this@HomeFragment)
                binding.rvProducts.adapter = productsAdapter
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
        val cart = Cart(
            name = model.name,
            id = model.id,
            product_id = model.product_id,
            sku = model.sku,
            image = model.image,
            thumb = model.thumb,
            zoom_thumb = model.zoom_thumb,
            description = model.description,
            quantity = model.qty,
            price = model.price,
            special = model.special
        )
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.cartDao().insert(cart)
        }

    }

}