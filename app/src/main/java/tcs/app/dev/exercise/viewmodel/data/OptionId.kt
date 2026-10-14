package tcs.app.dev.exercise.viewmodel.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@JvmInline
value class OptionId(val value: String) : Parcelable
