package tcs.app.dev.homework1.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface Discount : Parcelable {
    fun apply(cart: Cart, price: Euro): Euro

    @JvmInline
    value class Percentage(val value: UInt) : Discount {
        init {
            require(value in 0u..100u)
        }

        override fun apply(cart: Cart, price: Euro): Euro {
            val percentageToPay = 100u - value
            val cents = price.cents * percentageToPay / 100u
            return Euro(cents)
        }
    }

    @JvmInline
    value class Fixed(val amount: Euro) : Discount {
        override fun apply(cart: Cart, price: Euro): Euro = price - amount
    }

    data class Bundle(
        val itemId: ItemId,
        val amountItemsGet: UInt,
        val amountItemsPay: UInt
    ) : Discount {
        init {
            require(amountItemsGet > amountItemsPay)
        }

        val amountFreeItems get() = amountItemsGet - amountItemsPay

        /** Only complete bundles qualify; leftover items are paid at full price. */
        fun freeItems(quantity: UInt): UInt = quantity / amountItemsGet * amountFreeItems

        override fun apply(cart: Cart, price: Euro): Euro {
            val quantity = cart.items[itemId] ?: return price
            val itemPrice = cart.shop.item(itemId).price
            return price - itemPrice * freeItems(quantity)
        }
    }
}

/** Apply one best bundle per item, then all fixed and percentage discounts. */
fun List<Discount>.effectiveFor(cart: Cart): List<Discount> {
    val bundles = filterIsInstance<Discount.Bundle>()
        .groupBy { it.itemId }
        .mapNotNull { (itemId, offers) ->
            offers.maxByOrNull { it.freeItems(cart.items[itemId] ?: 0u) }
        }
    val fixed = filterIsInstance<Discount.Fixed>()
    val percentage = filterIsInstance<Discount.Percentage>()

    return bundles + fixed + percentage
}
