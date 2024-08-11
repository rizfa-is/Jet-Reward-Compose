package com.issog.jetrewardcompose.ui.screen.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.issog.jetrewardcompose.R
import com.issog.jetrewardcompose.di.Injection
import com.issog.jetrewardcompose.ui.ViewModelFactory
import com.issog.jetrewardcompose.ui.common.UiState
import com.issog.jetrewardcompose.ui.components.CartItem
import com.issog.jetrewardcompose.ui.components.OrderButton

@Composable
fun CartScreen(
    viewModel: CartViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideJetRewardRepository()
        )
    ),
    onOrderButtonClicked: (String) -> Unit,
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when(uiState) {
            is UiState.Loading -> {
                viewModel.getAddedOrderRewards()
            }
            is UiState.Success -> {
                CartContent(
                    state = uiState.data,
                    onProductChanged = { rewardId, count ->
                        viewModel.updateOrderReward(rewardId, count)
                    },
                    onOrderButtonClicked = onOrderButtonClicked
                )
            }
            is UiState.Error -> { }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartContent(
    state: CartState,
    onProductChanged: (rewardId: Long, count: Int) -> Unit,
    onOrderButtonClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val shareMessage = stringResource(
        R.string.share_message,
        state.orderReward.count(),
        state.totalRequiredPoint
    )
    Column (
        modifier = modifier.fillMaxSize()
    ){
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.menu_cart),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            },
            windowInsets = WindowInsets(
                top = 0.dp,
                bottom = 0.dp
            )
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(state.orderReward, key = { it.reward.id }) { item ->
                CartItem(
                    rewardId = item.reward.id,
                    image = item.reward.image,
                    title = item.reward.title,
                    totalPoint = item.reward.requiredPoint * item.count,
                    count = item.count,
                    onProductCountChanged = onProductChanged
                )
                Divider()
            }
        }
        OrderButton(
            text = stringResource(R.string.total_order, state.totalRequiredPoint),
            enabled = state.orderReward.isNotEmpty(),
            onClick = {
                      onOrderButtonClicked.invoke(shareMessage)
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}