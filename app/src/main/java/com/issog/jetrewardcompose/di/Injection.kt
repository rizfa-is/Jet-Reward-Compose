package com.issog.jetrewardcompose.di

import com.issog.jetrewardcompose.data.JetRewardRepository

object Injection {
    fun provideJetRewardRepository(): JetRewardRepository =
        JetRewardRepository.getInstance()
}