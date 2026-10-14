package tcs.app.dev.exercise.testing.data

object MockData {
    val Apple = ItemId("apple")
    val Banana = ItemId("banana")

    val Onion = ItemId("onion")

    private val ExampleItems = listOf(
        Item(Apple, "Apple", 60u.cents),
        Item(Banana, "Banana", 90u.cents),
        Item(Onion, "Onion", 40u.cents)
    )

    val ExampleCart =
        Cart(Shop(items = ExampleItems), items = mapOf(Apple to 1u, Banana to 1u))

    val EmptyExampleCart =
        Cart(Shop(items = ExampleItems))
}
