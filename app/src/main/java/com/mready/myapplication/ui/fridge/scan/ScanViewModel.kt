package com.mready.myapplication.ui.fridge.scan

import androidx.camera.core.ImageCapture
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor() : ViewModel() {

    private val _capturedImagePathFlow = MutableStateFlow("")
    val capturedImagePathFlow: StateFlow<String> = _capturedImagePathFlow

    private val _titleFlow = MutableStateFlow("")
    val titleFlow: StateFlow<String> = _titleFlow

    private lateinit var cameraHandler: CameraHandler

    fun updateTitle(title: String) {
        _titleFlow.value = title
    }

    fun updateCapturedImage(image: String) {
        _capturedImagePathFlow.value = image
    }

    fun setCameraHandler(cameraHandler: CameraHandler) {
        this.cameraHandler = cameraHandler
    }

    fun takePhoto(imageCapture: ImageCapture, title: String, onDoneTakingPhoto: (String) -> Unit) {
        cameraHandler.takePhoto(
            imageCapture = imageCapture,
            title = title,
            onFileCreated = { file ->
                updateCapturedImage(file.absolutePath)
            },
            onPhotoTaken = {
                updateTitle(it)
                onDoneTakingPhoto(it)
            }
        )
    }
}