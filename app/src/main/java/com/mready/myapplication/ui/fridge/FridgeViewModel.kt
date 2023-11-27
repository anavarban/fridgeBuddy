package com.mready.myapplication.ui.fridge

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import com.mready.myapplication.data.FridgeIngredientsRepo
import com.mready.myapplication.models.Ingredient
import com.mready.myapplication.models.toIngredient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import com.mready.myapplication.auth.AuthRepository
import com.mready.myapplication.models.toFridgeIngredient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

@HiltViewModel
class FridgeViewModel @Inject constructor(
    private val fridgeIngredientsRepo: FridgeIngredientsRepo,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _fridgeFlow =
        MutableStateFlow<FridgeIngredientsUiState>(FridgeIngredientsUiState.Loading)
    val fridgeFlow = _fridgeFlow

    val currentUser = authRepository.currentUser.value

    init {
        _fridgeFlow.update { FridgeIngredientsUiState.Loading }
        loadIngredients()
    }

    fun loadIngredients() {
        viewModelScope.launch {
            val list = fridgeIngredientsRepo.getUserIngredients(currentUser?.email ?: "")
                .stateIn(viewModelScope).value
            _fridgeFlow.update {
                FridgeIngredientsUiState.Success(list.map { it.toIngredient() })
            }
        }
    }

    fun deleteIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            fridgeIngredientsRepo.deleteIngredient(ingredient.id)
            loadIngredients()
        }
    }

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    fun recognise(
        textRecognizer: TextRecognizer,
        imageCapture: ImageCapture,
        executor: Executor
    ): String? {
        var ret: String? = null
        imageCapture.takePicture(
            executor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {

                    super.onCaptureSuccess(image)
                    image.image?.let {
                        val img = InputImage.fromMediaImage(it, image.imageInfo.rotationDegrees)
                        textRecognizer.process(img)
                            .addOnSuccessListener { visionText ->
                                // Task completed successfully
                                ret = visionText.text
                            }
                            .addOnFailureListener { e ->
                                // Task failed with an exception
                                // ...
                                throw e
                            }
                            .addOnCompleteListener {
                                image.close()
                            }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    throw exception
                }
            }
        )
        viewModelScope.launch{ delay(2000) }
        return ret
    }

}

sealed class FridgeIngredientsUiState {
    object Loading : FridgeIngredientsUiState()
    data class Success(val ingredients: List<Ingredient>?) : FridgeIngredientsUiState()
    object Error : FridgeIngredientsUiState()
}
