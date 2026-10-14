package tcs.app.dev.exercise.testing.data

import kotlinx.serialization.Serializable

@Serializable
data class Shop(val items: List<Item>) {
    init {
        require(items.map { it.id }.distinct().size == items.size) {
            "Item IDs must be unique"
        }
    }

    val itemIds get() = items.map { it.id }

    fun item(itemId: ItemId): Item = items.first { it.id == itemId }
}
