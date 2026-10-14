package tcs.app.dev.homework1.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Shop(val items: List<Item>) : Parcelable {
    init {
        require(items.map { it.id }.distinct().size == items.size) {
            "Item IDs must be unique"
        }
    }

    val itemIds get() = items.map { it.id }

    fun item(itemId: ItemId): Item = items.first { it.id == itemId }
}
