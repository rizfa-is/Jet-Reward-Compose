package com.issog.jetrewardcompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issog.jetrewardcompose.data.JetRewardRepository
import com.issog.jetrewardcompose.ui.screen.cart.CartViewModel
import com.issog.jetrewardcompose.ui.screen.home.HomeViewModel

class ViewModelFactory(private val repository: JetRewardRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                 HomeViewModel(repository) as T
            modelClass.isAssignableFrom(CartViewModel::class.java) ->
                CartViewModel(repository) as T
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}