package tcs.app.dev.exercise.testing.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/** Stable identity, independent of an item's name or price. */
@Serializable
@Parcelize
@JvmInline
value class ItemId(val value: String) : Parcelable
