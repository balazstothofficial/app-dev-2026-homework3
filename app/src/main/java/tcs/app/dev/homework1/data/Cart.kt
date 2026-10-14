package tcs.app.dev.homework1.data

import android.os.Parcelable
import androidx.compose.runtime.saveable.rememberSaveable
import kotlin.getValue
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * I made the cart and every type inside [Parcelable],
 * such that it can be stored using [rememberSaveable].
 * In a real app it would be stored persistently.
 * We'll do that in the next homework.
 */
@Parcelize
data class Cart(
    val shop: Shop,
    val items: Map<ItemId, UInt> = emptyMap(),
    // Keep every selected offer; the best bundle can change with the quantity.
    val discounts: List<Discount> = emptyList()
) : Parcelable {
    init {
        require(shop.itemIds.containsAll(items.keys))
    }

    val effectiveDiscounts: List<Discount>
        get() = discounts.effectiveFor(this)

    @IgnoredOnParcel
    val price: Euro by lazy {
        val price = items
            .map { (itemId, amount) -> shop.item(itemId).price * amount }
            .fold(0u.euro, Euro::plus)

        effectiveDiscounts
            .fold(price) { total, price -> price.apply(this, total) }
    }

    val itemCount: UInt
        get() = items.values.sum()

    val discountsCount: UInt
        get() = discounts.size.toUInt()

    val totalCount: UInt
        get() = discountsCount + itemCount
}

fun Cart.update(other: Pair<ItemId, UInt>): Cart = copy(items = items + other)

operator fun Cart.plus(other: Pair<ItemId, UInt>): Cart {
    val (itemId, amount) = other

    val currentAmount = items[itemId] ?: 0u
    val pair = other.copy(second = amount + currentAmount)

    return copy(items = items + pair)
}

operator fun Cart.plus(other: ItemId): Cart = this + (other to 1u)

operator fun Cart.plus(other: Discount): Cart = copy(discounts = this.discounts + other)

operator fun Cart.plus(other: List<Discount>): Cart = copy(discounts = this.discounts + other)

operator fun Cart.minus(other: ItemId): Cart = copy(items = this.items - other)

operator fun Cart.minus(other: Discount): Cart = copy(discounts = this.discounts - other)
