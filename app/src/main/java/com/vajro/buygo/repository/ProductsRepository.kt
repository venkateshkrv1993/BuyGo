package com.vajro.buygo.repository

import com.vajro.buygo.MainApplication
import com.vajro.buygo.models.ProductListResponse
import com.vajro.buygo.network.ApiServices
import com.vajro.buygo.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductsRepository {

    @Inject
    lateinit var apiServices: ApiServices

    init {
        MainApplication.getMainApplication().mainComponent.inject(this)
    }

    suspend fun getProducts(): Resource<ProductListResponse> {
        return withContext(Dispatchers.IO) {
            apiServices.getProducts()
        }
    }

}