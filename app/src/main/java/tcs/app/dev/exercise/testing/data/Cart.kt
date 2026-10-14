package tcs.app.dev.exercise.testing.data

import kotlin.getValue
import kotlinx.serialization.Serializable

@Serializable
data class Cart(
    val shop: Shop,
    val items: Map<ItemId, UInt> = emptyMap()
) {
    init {
        require(shop.itemIds.containsAll(items.keys))
    }

    val price: Euro by lazy {
        items
            .map { (itemId, amount) -> shop.item(itemId).price * amount }
            .fold(0u.euro, Euro::plus)
    }

    val itemCount: UInt
        get() = items.values.sum()

    fun item(itemId: ItemId) = shop.item(itemId)

}

fun Cart.update(other: Pair<ItemId, UInt>): Cart = copy(items = items + other)

operator fun Cart.plus(other: Pair<ItemId, UInt>): Cart {
    val (itemId, amount) = other

    val currentAmount = items[itemId] ?: 0u
    val pair = other.copy(second = amount + currentAmount)

    return copy(items = items + pair)
}

operator fun Cart.plus(other: ItemId): Cart = this + (other to 1u)

operator fun Cart.minus(other: ItemId): Cart = copy(items = this.items - other)
