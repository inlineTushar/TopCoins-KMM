@file:OptIn(ExperimentalMaterial3Api::class)

package com.tushar.priceupdate

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tushar.feature.priceupdate.generated.resources.Res
import com.tushar.feature.priceupdate.generated.resources.feature_priceupdate_title
import com.tushar.feature.priceupdate.generated.resources.ic_arrow_down_24
import com.tushar.feature.priceupdate.generated.resources.ic_arrow_up_24
import com.tushar.ui.component.AppBar
import com.tushar.ui.component.ProgressBarComposable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.random.Random
import androidx.navigation3.runtime.NavBackStack
import com.tushar.navigation.Route

@Composable
fun PriceLiveUpdateScreen(
    backStack: NavBackStack<Route>,
    modifier: Modifier = Modifier,
    vm: PriceLiveUpdateViewModel = koinViewModel()
) {
    val state by vm.uiState.collectAsState()

    PriceUpdateComposable(
        state = state,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLast()
            }
        },
        modifier = modifier
    )
}

@Composable
internal fun PriceUpdateComposable(
    state: PriceLiveUpdateUiState,
    onBack: () -> Unit,
    modifier: Modifier
) {
    Scaffold(
        modifier = modifier
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
            ),
        topBar = {
            AppBar(
                title = stringResource(Res.string.feature_priceupdate_title),
                isBackVisible = true,
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (state) {
                PriceLiveUpdateUiState.Loading -> ProgressBarComposable()
                is PriceLiveUpdateUiState.Tick ->
                    PriceComposable(
                        symbol = state.symbol,
                        isHiked = state.isHiked,
                        priceWithCurrency = state.price
                    )
            }
        }
    }
}

@Composable
internal fun PriceComposable(
    symbol: String,
    isHiked: Boolean?,
    priceWithCurrency: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = modifier) {
            Column {
                Text(
                    text = symbol,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge
                        .copy(fontSize = 28.sp, fontWeight = FontWeight.Thin)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = priceWithCurrency,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge
                            .copy(
                                fontSize = 48.sp,
                                brush = Brush.linearGradient(
                                    colors = remember {
                                        listOf(
                                            Color(
                                                Random.nextInt(256),
                                                Random.nextInt(256),
                                                Random.nextInt(256)
                                            ),
                                            Color(
                                                Random.nextInt(256),
                                                Random.nextInt(256),
                                                Random.nextInt(256)
                                            )
                                        )
                                    },
                                    tileMode = TileMode.Repeated
                                )
                            )
                    )

                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        PriceHikeIndicator(
                            visible = isHiked == true,
                            key = priceWithCurrency
                        )
                        PriceDownIndicator(
                            visible = isHiked == false,
                            key = priceWithCurrency
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PriceHikeIndicator(
    visible: Boolean,
    key: String,
    modifier: Modifier = Modifier
) {
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(key) {
        if (visible) {
            alpha.snapTo(1f)
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 500)
            )
        } else {
            alpha.snapTo(0f)
        }
    }

    Icon(
        painter = painterResource(Res.drawable.ic_arrow_up_24),
        contentDescription = "Price hike indicator",
        tint = Color.Green,
        modifier = modifier.alpha(alpha.value)
    )
}

@Composable
fun PriceDownIndicator(
    visible: Boolean,
    key: String,
    modifier: Modifier = Modifier
) {
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(key) {
        if (visible) {
            alpha.snapTo(1f)
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 500)
            )
        } else {
            alpha.snapTo(0f)
        }
    }

    Icon(
        painter = painterResource(Res.drawable.ic_arrow_down_24),
        contentDescription = "Price down indicator",
        tint = Color.Red,
        modifier = modifier.alpha(alpha.value)
    )
}
