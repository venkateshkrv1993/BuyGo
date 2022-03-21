package com.vajro.buygo.network

import com.vajro.buygo.models.ProductListResponse
import retrofit2.http.GET

interface ApiServices {

    @GET("v2/5def7b172f000063008e0aa2")
    suspend fun getProducts(): Resource<ProductListResponse>

}