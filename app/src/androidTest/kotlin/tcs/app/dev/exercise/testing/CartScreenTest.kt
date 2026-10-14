package tcs.app.dev.exercise.testing

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.DataStore
import androidx.test.core.app.ApplicationProvider
import org.junit.Rule
import org.junit.Test
import tcs.app.dev.R
import tcs.app.dev.exercise.testing.data.CartSerializer
import tcs.app.dev.exercise.testing.data.Cart
import tcs.app.dev.exercise.testing.data.MockData
import java.io.File
import java.util.UUID

/**
 * # UI Testing
 *
 * UI tests for ComposeUI.
 *
 * ## Tasks
 * - Familiarize yourself with the [CartScreen] implementation.
 * - What are the tests doing? Try to change values, such that a test fails.
 * - What is the problem if we use [CartScreen] directly in the tests?
 *   How is that problem solved?
 *
 * ## Resources
 * - [ComposeUI Testing](https://developer.android.com/develop/ui/compose/testing)
 */
class CartScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun increaseTest() {
        composeTestRule.setContent {
            TestCartScreen()
        }

        assertTotalAmount(2u)

        composeTestRule
            .onAllNodesWithContentDescription(increaseDescription)
            .onFirst()
            .performClick()

        assertTotalAmount(3u)
    }

    @Test
    fun decreaseTest() {
        composeTestRule.setContent {
            TestCartScreen()
        }

        assertTotalAmount(2u)

        composeTestRule
            .onAllNodesWithContentDescription(decreaseDescription)
            .onFirst()
            .performClick()

        assertTotalAmount(1u)
    }

    private fun assertTotalAmount(amount: UInt) {
        // DataStore writes run outside Compose's test clock.
        val text = totalAmountText(amount)
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule.onAllNodesWithText(text).fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithText(text).assertExists()
    }

    @Composable
    private fun TestCartScreen() {
        val viewModel = remember { CartViewModel(dataStore(), MockData.ExampleCart) }
        CartScreen(viewModel = viewModel)
    }

    private val context: Context get() = ApplicationProvider.getApplicationContext()

    private fun dataStore(): DataStore<Cart> {
        val file = File(context.filesDir, "cart-test-${UUID.randomUUID()}.json")
        return DataStoreFactory.create(CartSerializer, produceFile = { file })
    }

    private val decreaseDescription
        get() = context.getString(R.string.description_decrease_amount)

    private val increaseDescription
        get() = context.getString(R.string.description_increase_amount)

    private fun totalAmountText(amount: UInt) = context.getString(R.string.total_amount, amount)
}
