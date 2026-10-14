package tcs.app.dev.exercise.viewmodel

import android.graphics.Bitmap

sealed interface ImageState {
    data object Loading : ImageState
    data class Loaded(val bitmap: Bitmap) : ImageState
    data object Failed : ImageState
}
