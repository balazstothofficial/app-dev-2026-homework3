package tcs.app.dev.exercise.testing.data

import kotlinx.serialization.Serializable

@Serializable
data class Item(val id: ItemId, val name: String, val price: Euro)
