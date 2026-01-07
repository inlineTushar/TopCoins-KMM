package com.tushar.coinlist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tushar.coinlist.formatter.PercentageFormatterContract
import com.tushar.coinlist.formatter.TimeFormatterContract
import com.tushar.core.formatter.CurrencyFormatterContract
import com.tushar.core.model.BigDecimal
import com.tushar.domain.GetCoinUseCase
import com.tushar.domain.model.CoinCurrency
import com.tushar.domain.model.CoinDomainModel
import com.tushar.domain.model.CoinsDomainModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.Instant
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Android instrumentation tests for CoinListScreen Composables.
 *
 * These tests verify the UI rendering and interactions of the coin list feature.
 */
@RunWith(AndroidJUnit4::class)
class CoinListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockUseCase: GetCoinUseCase
    private lateinit var mockCurrencyFormatter: CurrencyFormatterContract
    private lateinit var mockPercentFormatter: PercentageFormatterContract
    private lateinit var mockTimeFormatter: TimeFormatterContract

    private val testCoins = CoinsDomainModel(
        timestamp = Instant.fromEpochMilliseconds(1705329045000L),
        coins = listOf(
            CoinDomainModel(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                price = BigDecimal("50000.00"),
                changePercent24Hr = 5.23,
                coinCurrency = CoinCurrency(
                    id = "eur",
                    code = "EUR",
                    symbol = "€",
                    type = "fiat",
                    rateInUsd = BigDecimal("1.07")
                )
            ),
            CoinDomainModel(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                price = BigDecimal("3000.00"),
                changePercent24Hr = -2.15,
                coinCurrency = CoinCurrency(
                    id = "eur",
                    code = "EUR",
                    symbol = "€",
                    type = "fiat",
                    rateInUsd = BigDecimal("1.07")
                )
            )
        )
    )

    @Before
    fun setUp() {
        mockUseCase = mockk()
        mockCurrencyFormatter = mockk()
        mockPercentFormatter = mockk()
        mockTimeFormatter = mockk()

        // Setup default mock responses
        every { mockCurrencyFormatter.format(any(), any()) } returns "€50,000.00"
        every { mockPercentFormatter.format(any(), any()) } returns "+5.23%"
        every { mockTimeFormatter.format(any(), any()) } returns "12:00:00"

        coEvery {
            mockUseCase.sortByBestPriceChange(any(), any())
        } returns Result.success(testCoins)

        coEvery {
            mockUseCase.sortByWorstPriceChange(any(), any())
        } returns Result.success(testCoins)
    }

    @Test
    fun coinListScreen_contentState_displaysCoinList() {
        // Given - Setup specific formatting for each coin
        every {
            mockCurrencyFormatter.format(BigDecimal("50000.00"), any())
        } returns "€50,000.00"
        every {
            mockCurrencyFormatter.format(BigDecimal("3000.00"), any())
        } returns "€3,000.00"
        every { mockPercentFormatter.format(5.23, any()) } returns "+5.23%"
        every { mockPercentFormatter.format(-2.15, any()) } returns "-2.15%"
        every { mockTimeFormatter.format(any(), any()) } returns "12:00:00"

        // When
        val viewModel = CoinListViewModel(
            useCase = mockUseCase,
            currencyFormatter = mockCurrencyFormatter,
            percentageFormatter = mockPercentFormatter,
            timeFormatter = mockTimeFormatter
        )

        composeTestRule.setContent {
            CoinListScreen(navController = mockk(), vm = viewModel)
        }

        // Wait for async initialization
        composeTestRule.waitForIdle()

        // Give time for flow collection and state updates
        composeTestRule.waitForIdle()

        // Then - Verify Bitcoin is displayed
        composeTestRule.onNodeWithText("Bitcoin", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("BTC", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("€50,000.00", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("+5.23%", useUnmergedTree = true).assertIsDisplayed()

        // Verify Ethereum is displayed
        composeTestRule.onNodeWithText("Ethereum", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("ETH", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("€3,000.00", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("-2.15%", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun coinListScreen_contentState_displaysUpdateTime() {
        // Given
        val testTimestamp = Instant.fromEpochMilliseconds(1705329045000L)
        every { mockTimeFormatter.format(testTimestamp, any()) } returns "14:30:00"

        // When
        val viewModel = CoinListViewModel(
            useCase = mockUseCase,
            currencyFormatter = mockCurrencyFormatter,
            percentageFormatter = mockPercentFormatter,
            timeFormatter = mockTimeFormatter
        )

        composeTestRule.setContent {
            CoinListScreen(navController = mockk(), vm = viewModel)
        }
        composeTestRule.waitForIdle()

        // Then - Update time should be visible
        composeTestRule.onNodeWithText("14:30:00", substring = true, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun coinListScreen_appBar_displaysCoinListTitle() {
        // When
        val viewModel = CoinListViewModel(
            useCase = mockUseCase,
            currencyFormatter = mockCurrencyFormatter,
            percentageFormatter = mockPercentFormatter,
            timeFormatter = mockTimeFormatter
        )

        composeTestRule.setContent {
            CoinListScreen(navController = mockk(), vm = viewModel)
        }

        // Then - Title should be visible immediately (app bar is always shown)
        composeTestRule.onNodeWithText("Top Coin", substring = true, ignoreCase = true)
            .assertIsDisplayed()
    }

    @Test
    fun coinItemComposable_accessible_hasProperSemantics() {
        // When
        composeTestRule.setContent {
            CoinItemComposable(
                coinName = "Bitcoin",
                coinSymbol = "BTC",
                coinPrice = "€50,000.00",
                coinChange = "+5.23%"
            )
        }

        // Then - Semantic descriptions should exist
        composeTestRule.onNodeWithContentDescription(
            "Coin item: Bitcoin (BTC), price €50,000.00, change in last 24 hours +5.23%"
        ).assertIsDisplayed()
    }

    @Test
    fun coinItemComposable_displaysAllInformation() {
        // When
        composeTestRule.setContent {
            CoinItemComposable(
                coinName = "Ethereum",
                coinSymbol = "ETH",
                coinPrice = "€3,000.00",
                coinChange = "-2.15%"
            )
        }

        // Then
        composeTestRule.onNodeWithText("Ethereum").assertIsDisplayed()
        composeTestRule.onNodeWithText("ETH").assertIsDisplayed()
        composeTestRule.onNodeWithText("€3,000.00").assertIsDisplayed()
        composeTestRule.onNodeWithText("-2.15%").assertIsDisplayed()
    }

    @Test
    fun coinItemComposable_negativeChange_displayed() {
        // When
        composeTestRule.setContent {
            CoinItemComposable(
                coinName = "Cardano",
                coinSymbol = "ADA",
                coinPrice = "€0.50",
                coinChange = "-10.50%"
            )
        }

        // Then
        composeTestRule.onNodeWithText("-10.50%").assertIsDisplayed()
    }

    @Test
    fun coinItemComposable_positiveChange_displayed() {
        // When
        composeTestRule.setContent {
            CoinItemComposable(
                coinName = "Solana",
                coinSymbol = "SOL",
                coinPrice = "€100.00",
                coinChange = "+15.75%"
            )
        }

        // Then
        composeTestRule.onNodeWithText("+15.75%").assertIsDisplayed()
    }

    @Test
    fun coinItemComposable_displaysLongCoinName() {
        // When
        composeTestRule.setContent {
            CoinItemComposable(
                coinName = "A Very Long Cryptocurrency Name That Might Wrap",
                coinSymbol = "LONG",
                coinPrice = "€1.23",
                coinChange = "+0.01%"
            )
        }

        // Then - Long name should be displayed
        composeTestRule.onNodeWithText(
            "A Very Long Cryptocurrency Name That Might Wrap",
            substring = true
        ).assertIsDisplayed()
    }

    @Test
    fun coinItemComposable_displaysLargePrice() {
        // When
        composeTestRule.setContent {
            CoinItemComposable(
                coinName = "Expensive Coin",
                coinSymbol = "EXP",
                coinPrice = "€999,999.99",
                coinChange = "+100.00%"
            )
        }

        // Then
        composeTestRule.onNodeWithText("€999,999.99").assertIsDisplayed()
        composeTestRule.onNodeWithText("+100.00%").assertIsDisplayed()
    }

    @Test
    fun coinItemComposable_displaysSmallPrice() {
        // When
        composeTestRule.setContent {
            CoinItemComposable(
                coinName = "Cheap Coin",
                coinSymbol = "CHEAP",
                coinPrice = "€0.0001",
                coinChange = "-50.00%"
            )
        }

        // Then
        composeTestRule.onNodeWithText("€0.0001").assertIsDisplayed()
        composeTestRule.onNodeWithText("-50.00%").assertIsDisplayed()
    }
}
