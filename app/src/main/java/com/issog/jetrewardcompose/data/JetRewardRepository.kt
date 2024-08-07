package com.issog.jetrewardcompose.data

import com.issog.jetrewardcompose.data.source.FakeRewardDataSource
import com.issog.jetrewardcompose.model.OrderReward
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class JetRewardRepository {

    private val orderRewards = mutableListOf<OrderReward>()

    init {
        orderRewards.ifEmpty {
            FakeRewardDataSource.dummyRewards.forEach { reward ->
                orderRewards.add(OrderReward(reward, 0))
            }
        }
    }

    fun getAllRewards(): Flow<List<OrderReward>> {
        return flowOf(orderRewards)
    }

    fun getOrderRewardById(rewardId: Long): Flow<OrderReward> {
        return flowOf(
            orderRewards.first {
                it.reward.id == rewardId
            }
        )
    }

    fun updateOrderReward(rewardId: Long, newCountValue: Int): Flow<Boolean> {
        val index = orderRewards.indexOfFirst { it.reward.id == rewardId }
        val result = if (index >= 0) {
            val orderReward = orderRewards[index]
            orderRewards[index] = orderReward.copy(reward = orderReward.reward, count = newCountValue)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    fun getAddedOrderRewards(): Flow<List<OrderReward>> {
        return getAllRewards()
            .map { orderRewards ->
                orderRewards.filter { orderReward ->
                    orderReward.count != 0
                }
            }
    }

    companion object {
        @Volatile
        private var instance: JetRewardRepository? = null

        fun getInstance(): JetRewardRepository =
            instance ?: synchronized(this) {
                JetRewardRepository().apply {
                    instance = this
                }
            }
    }
}