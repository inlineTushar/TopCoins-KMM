package com.tushar.coinlist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tushar.coinlist.CoinItemComposable
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoinItemComposableTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun coinItem_displaysAllFields() {
        // When
        composeTestRule.setContent {
            CoinItemComposable(
                coinName = "Bitcoin",
                coinSymbol = "BTC",
                coinPrice = "€50,000.00",
                coinChange = "+5.23"
            )
        }

        // Then - All fields are displayed
        composeTestRule.onNodeWithText("Bitcoin").assertIsDisplayed()
        composeTestRule.onNodeWithText("BTC").assertIsDisplayed()
        composeTestRule.onNodeWithText("€50,000.00").assertIsDisplayed()
        composeTestRule.onNodeWithText("+5.23").assertIsDisplayed()
    }

    @Test
    fun coinItem_hasAccessibilityLabels() {
        composeTestRule.setContent {
            CoinItemComposable(
                coinName = "Ethereum",
                coinSymbol = "ETH",
                coinPrice = "€3,000.00",
                coinChange = "-2.15"
            )
        }

        composeTestRule.onNodeWithContentDescription(
            label = "Coin item: Ethereum (ETH), price €3,000.00, change in last 24 hours -2.15"
        ).assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Coin name: Ethereum").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Symbol: ETH").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Price: €3,000.00").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Change in last 24 hours: -2.15")
            .assertIsDisplayed()
    }

    @Test
    fun coinItem_displaysPositiveChange() {
        composeTestRule.setContent {
            CoinItemComposable(
                coinName = "Solana",
                coinSymbol = "SOL",
                coinPrice = "€100.50",
                coinChange = "+12.45"
            )
        }

        composeTestRule.onNodeWithText("+12.45").assertIsDisplayed()
    }

    @Test
    fun coinItem_displaysNegativeChange() {
        composeTestRule.setContent {
            CoinItemComposable(
                coinName = "Cardano",
                coinSymbol = "ADA",
                coinPrice = "€0.50",
                coinChange = "-8.92"
            )
        }

        composeTestRule.onNodeWithText("-8.92").assertIsDisplayed()
    }

    @Test
    fun coinItem_displaysLongCoinName() {
        composeTestRule.setContent {
            CoinItemComposable(
                coinName = "A Very Long Cryptocurrency Name That Might Wrap",
                coinSymbol = "LONG",
                coinPrice = "€1.23",
                coinChange = "+0.01"
            )
        }

        composeTestRule.onNodeWithText(
            "A Very Long Cryptocurrency Name That Might Wrap",
            substring = true
        ).assertIsDisplayed()
    }

    @Test
    fun coinItem_displaysLargePrice() {
        composeTestRule.setContent {
            CoinItemComposable(
                coinName = "Expensive Coin",
                coinSymbol = "EXP",
                coinPrice = "€1,234,567.89",
                coinChange = "+0.50"
            )
        }

        composeTestRule.onNodeWithText("€1,234,567.89").assertIsDisplayed()
    }

    @Test
    fun coinItem_displaysSmallPrice() {
        composeTestRule.setContent {
            CoinItemComposable(
                coinName = "Cheap Coin",
                coinSymbol = "CHEAP",
                coinPrice = "€0.0001",
                coinChange = "-0.01"
            )
        }

        composeTestRule.onNodeWithText("€0.0001").assertIsDisplayed()
    }

    @Test
    fun coinItem_displaysZeroChange() {
        composeTestRule.setContent {
            CoinItemComposable(
                coinName = "Stable Coin",
                coinSymbol = "STBL",
                coinPrice = "€1.00",
                coinChange = "0.00"
            )
        }

        composeTestRule.onNodeWithText("0.00").assertIsDisplayed()
    }
}
