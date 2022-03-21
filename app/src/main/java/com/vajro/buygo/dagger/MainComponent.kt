package com.vajro.buygo.dagger

import com.vajro.buygo.repository.ProductsRepository
import com.vajro.buygo.viewmodels.HomeViewModel
import dagger.Component

@Component(modules = [Module::class])
interface MainComponent {

    fun inject(repository: ProductsRepository)

    fun inject(viewModel: HomeViewModel)

}