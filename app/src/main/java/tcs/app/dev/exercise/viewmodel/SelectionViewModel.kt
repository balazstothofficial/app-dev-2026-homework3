package tcs.app.dev.exercise.viewmodel

import androidx.lifecycle.ViewModel
import tcs.app.dev.exercise.viewmodel.data.OptionId
import tcs.app.dev.exercise.viewmodel.data.getImage
import tcs.app.dev.exercise.viewmodel.data.getOptions
import tcs.app.dev.exercise.viewmodel.data.getPreviewImage

/**
 * # Implement a ViewModel for University Selection
 *
 * In this exercise, you will implement a `SelectionViewModel` that manages UI state
 * for selecting a university and displaying its details. The ViewModel should expose
 * observable state using Kotlin Flows and handle asynchronous image loading.
 *
 * ## Tasks
 *
 * 1. **Define the UI state model**
 *    - Create an immutable `data class State` inside `SelectionViewModel` containing:
 *      - `options: List<UIOption>` – universities and their preview image states (default: empty).
 *        `UIOption` wraps the API's `Option` together with an `ImageState`.
 *      - `selectionId: OptionId?` – the selected option ID (default: `null`).
 *      - Derive `selection: UIOption?` from the latest `options` using `selectionId`.
 *      - `selectionImage: ImageState` – the full image state (default: `ImageState.Loading`).
 *    - Use the sealed `ImageState`: `Loading`, `Loaded(bitmap)`, or `Failed`.
 *      Only display the full image state while a university is selected.
 *
 * 2. **Make the state observable**
 *    - Create a private property of type `MutableStateFlow<State>`.
 *    - Expose a public `StateFlow<State>` using `asStateFlow()` so the UI can collect it.
 *
 *  3. **Provide image-loading helpers**
 *    - Implement a suspend helper that loads an image using a supplied request function.
 *      Use it with [getImage] and [getPreviewImage] for full images and previews.
 *    - Execute them on a background dispatcher (`Dispatchers.IO`).
 *    - Cap retries; return `Loaded(bitmap)` on success and `Failed` after exhausted retries.
 *      Allow coroutine cancellation to propagate without turning it into `Failed`.
 *
 * 4. **Load and publish the options list**
 *    - Implement a suspend function `loadOptions()` to retrieve a list of `Option` objects
 *   using [getOptions].
 *    - Wrap the fetched options in `UIOption` so every preview starts in `Loading`.
 *    - Inside `coroutineScope`, launch a child coroutine for each preview and update
 *      its `UIOption` as soon as loading finishes.
 *    - Let the caller own the loading lifetime: cancellation stops all child requests.
 *
 * 5. **Handle selection and full image loading**
 *    - Implement a function `select(selection: OptionId?)`.
 *    - When the ID changes, update `selectionId` and reset `selectionImage` to `Loading`.
 *      Selecting the same ID again should leave the state unchanged.
 *    - Implement `suspend fun loadSelectionImage(selection: OptionId)` to load the full image.
 *      Skip loading if the ID is no longer selected or its image is already `Loaded`.
 *    - The screen calls this function from a `LaunchedEffect` keyed by the selection ID;
 *      changing the selection or leaving the screen cancels the previous request.
 *    - Set `Loading` before a request, then publish its `Loaded` or `Failed` result
 *      only if the same option is still selected.
 *
 *
 * ## Resources
 *
 * - [ViewModels](https://developer.android.com/topic/libraries/architecture/viewmodel)
 * - [Coroutine dispatchers](https://kotlinlang.org/docs/coroutines-basics.html#coroutine-dispatchers)
 * - [Coroutines](https://kotlinlang.org/docs/coroutines-guide.html)
 * - [ViewModel scope](https://developer.android.com/topic/libraries/architecture/coroutines#viewmodelscope)
 * - [StateFlow in ViewModels](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow#stateflow)
 * - [StateFlow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/)
 * - [Flow](https://kotlinlang.org/docs/flow.html)
 *
 */
class SelectionViewModel : ViewModel()
