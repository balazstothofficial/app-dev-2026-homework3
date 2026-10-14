package tcs.app.dev.homework1.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tcs.app.dev.R

private data class UIItem(
    val item: Item,
    @param:DrawableRes val image: Int,
    @param:StringRes val name: Int
)

/**
 * Mock items and associated resources.
 * In the next lecture we'll learn, how it works if we get this data dynamically.
 **/
object MockData {
    val Apple = ItemId("Apple")
    val Banana = ItemId("Banana")

    private val ExampleItems = listOf(
        UIItem(Item(Apple, 60u.cents), R.drawable.apple, R.string.name_apple),
        UIItem(Item(Banana, 90u.cents), R.drawable.banana, R.string.name_banana),
        UIItem(Item(ItemId("GoldenFig"), 16u.euro + 80u.cents), R.drawable.golden_fig, R.string.name_golden_fig),
        UIItem(Item(ItemId("Bread"), 1u.euro + 90u.cents), R.drawable.bread, R.string.name_bread),
        UIItem(Item(ItemId("Chair"), 49u.euro + 99u.cents), R.drawable.chair, R.string.name_chair),
        UIItem(Item(ItemId("Computer"), 1499u.euro + 99u.cents), R.drawable.computer, R.string.name_computer),
        UIItem(Item(ItemId("Knives"), 40u.euro), R.drawable.knives, R.string.name_knives),
        UIItem(Item(ItemId("Onion"), 40u.cents), R.drawable.onion, R.string.name_onion),
        UIItem(Item(ItemId("Scissor"), 3u.euro + 50u.cents), R.drawable.scissor, R.string.name_scissor),
        UIItem(Item(ItemId("Spaghetti"), 1u.euro), R.drawable.spaghetti, R.string.name_spaghetti),
        UIItem(Item(ItemId("Table"), 400u.euro), R.drawable.table, R.string.name_table),
        UIItem(Item(ItemId("Tomato"), 50u.cents), R.drawable.tomato, R.string.name_tomato)
    )

    val ExampleShop = Shop(
        items = ExampleItems.map { it.item }
    )

    val ExampleDiscounts = listOf(
        Discount.Fixed(3u.euro),
        Discount.Percentage(10u),
        Discount.Bundle(Apple, 5u, 3u),
        Discount.Bundle(Banana, 3u, 2u)
    )

    @StringRes
    fun getName(itemId: ItemId): Int = ExampleItems.first { uiItem -> uiItem.item.id == itemId }.name

    @DrawableRes
    fun getImage(itemId: ItemId): Int = ExampleItems.first { uiItem -> uiItem.item.id == itemId }.image
}
