package tcs.app.dev.homework1.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: ItemId,
    val price: Euro
) : Parcelable
