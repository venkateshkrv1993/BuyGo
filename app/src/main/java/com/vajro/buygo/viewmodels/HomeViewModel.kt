package com.vajro.buygo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vajro.buygo.MainApplication
import com.vajro.buygo.models.ProductListResponse
import com.vajro.buygo.network.Resource
import com.vajro.buygo.network.Status
import com.vajro.buygo.repository.ProductsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel : ViewModel() {

    var productsResponse = MutableLiveData<Resource<ProductListResponse>>()

    @Inject
    lateinit var productsRepository: ProductsRepository

    init {
        MainApplication.getMainApplication().mainComponent.inject(this)
    }

    fun getProducts() {
        productsResponse.postValue(Resource.loading(null))
        viewModelScope.launch {
            val response = productsRepository.getProducts()
            when (response.status) {
                Status.SUCCESS -> {
                    productsResponse.postValue(Resource.success(response.data))
                }
                Status.ERROR -> {
                    productsResponse.postValue(Resource.error(response.message ?: "", null))
                }
            }
        }

    }


}