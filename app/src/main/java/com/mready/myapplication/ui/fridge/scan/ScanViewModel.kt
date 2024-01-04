package com.mready.myapplication.ui.fridge.scan

import android.os.Environment
import android.util.Log
import android.util.Rational
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor() : ViewModel() {

    var capturedImageFlow = MutableStateFlow("")
    var titleFlow = MutableStateFlow("")

    fun getTitle(): String {
        return titleFlow.value
    }

    fun updateTitle(title: String) {
        titleFlow.value = title
    }

    fun getCapturedImage(): String {
        return capturedImageFlow.value
    }

    fun updateCapturedImage(image: String) {
        capturedImageFlow.value = image
    }

    //do these go here?
    fun takePhoto(
        context: android.content.Context,
        imageCapture: ImageCapture,
        title: String,
        onPhotoTaken: (String) -> Unit,
    ) {
        capturedImageFlow.value = title.take(10)
        imageCapture.setCropAspectRatio(Rational(1, 1))
        val photoFile = createImageFile(context, capturedImageFlow.value)
        imageCapture.takePicture(
            ImageCapture.OutputFileOptions.Builder(photoFile).build(),
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.d("ScanViewModel", "onImageSaved: ${outputFileResults.savedUri}")
                    titleFlow.value = title.take(10)
                    onPhotoTaken(title.take(10))
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.d("ScanViewModel", "onError: ${exception.message}")
                }
            }
        )
    }

    private fun createImageFile(context: android.content.Context, title: String): File {
        val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            title.ifEmpty {
                "JPEG_${System.currentTimeMillis()}"
            }, /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            capturedImageFlow.value = absolutePath
        }
    }
}