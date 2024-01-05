package com.mready.myapplication.ui.fridge.scan

import android.content.Context
import android.os.Environment
import android.util.Log
import android.util.Rational
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import java.io.File

class CameraHandler (private val context: Context) {

    fun takePhoto(
        imageCapture: ImageCapture,
        title: String,
        onFileCreated: (File) -> Unit,
        onPhotoTaken: (String) -> Unit,
    ) {
        imageCapture.setCropAspectRatio(Rational(1, 1))
        val photoFile = createImageFile(
            context = context,
            title = title.take(10),
            onFileCreated = onFileCreated
        )
        imageCapture.takePicture(
            ImageCapture.OutputFileOptions.Builder(photoFile).build(),
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.d("ScanViewModel", "onImageSaved: ${outputFileResults.savedUri}")
                    onPhotoTaken(title.take(10))
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.d("ScanViewModel", "onError: ${exception.message}")
                }
            }
        )
    }

    private fun createImageFile(context: Context, title: String, onFileCreated: (File) -> Unit): File {
        val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            title.ifEmpty {
                "JPEG_${System.currentTimeMillis()}"
            }, /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            onFileCreated(this)
        }
    }
}