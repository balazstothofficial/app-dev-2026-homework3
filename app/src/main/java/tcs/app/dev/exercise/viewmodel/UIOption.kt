package tcs.app.dev.exercise.viewmodel

import tcs.app.dev.exercise.viewmodel.data.Option

// Loading state belongs to the screen, not the API's university data.
data class UIOption(
    val option: Option,
    val previewImage: ImageState = ImageState.Loading
)
