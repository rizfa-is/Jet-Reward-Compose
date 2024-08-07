package com.issog.jetrewardcompose.ui.screen.cart

import com.issog.jetrewardcompose.model.OrderReward

data class CartState(
    val orderReward: List<OrderReward>,
    val totalRequiredPoint: Int
)
