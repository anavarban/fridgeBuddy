package com.mready.myapplication.ui.fridge.scan

import android.Manifest
import android.os.Build
import android.util.Log
import android.util.Size
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.LightAccent
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.Poppins
import java.util.concurrent.Executors

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanScreen(
    viewModel: ScanViewModel,
    onBack: () -> Unit = {},
    onTextRecognised: (text: String) -> Unit = {}
) {
    BackHandler {
        onBack()
    }

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val mediaPermissionState = rememberPermissionState(Manifest.permission.READ_MEDIA_IMAGES)

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(cameraPermissionState, mediaPermissionState) {

        val permissionResult = cameraPermissionState.status
        val mediaPermissionResult = mediaPermissionState.status

        if (!permissionResult.isGranted || !mediaPermissionResult.isGranted) {
            if (permissionResult.shouldShowRationale) {
                // Show a rationale if needed (optional)
            } else {
                // Request the permission
                cameraPermissionState.launchPermissionRequest()
                mediaPermissionState.launchPermissionRequest()
            }
        }
    }

    val cameraHandler = remember { CameraHandler(context) }
    DisposableEffect(cameraHandler) {
        viewModel.setCameraHandler(cameraHandler)
        onDispose { }
    }


    var shouldAnalyse by remember {
        mutableStateOf(false)
    }

    val cameraProviderFuture = remember(context) {
        ProcessCameraProvider.getInstance(context)
    }

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(bottom = 16.dp, start = 20.dp, end = 20.dp)
        ) {
            Text(
                modifier = Modifier.padding(top = 40.dp),
                text = stringResource(id = R.string.scan_screen_title),
                fontSize = 24.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 8.dp, end = 8.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    factory = { context ->
                        val cameraExecutor = Executors.newSingleThreadExecutor()
                        val previewView = PreviewView(context).also {
                            it.scaleType = PreviewView.ScaleType.FILL_START
                        }

                        cameraProviderFuture.addListener({
                            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                            val preview = Preview.Builder().build()
                            preview.setSurfaceProvider(previewView.surfaceProvider)

                            val previewWidth = previewView.layoutParams.width
                            val previewHeight = previewView.layoutParams.height

                            val imageCapture = ImageCapture.Builder().build()

                            val imageAnalyzer = ImageAnalysis.Builder()
                                .setTargetResolution(Size(previewWidth, previewHeight))
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()
                                .also {
                                    it.setAnalyzer(cameraExecutor, TextRecAnalyser { text ->
                                        if (text.isNotEmpty() && shouldAnalyse) {
                                            shouldAnalyse = false
                                            viewModel.takePhoto(
                                                title = text.replace("\n", " "),
                                                imageCapture = imageCapture,
                                                onDoneTakingPhoto =  onTextRecognised
                                            )
                                        }
                                    })
                                }

                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview,
                                    imageCapture,
                                    imageAnalyzer
                                )

                            } catch (exc: Exception) {
                                Log.e("DEBUG", "Use case binding failed", exc)
                            }
                        }, ContextCompat.getMainExecutor(context))
                        previewView
                    }
                )

                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .padding(16.dp),
                    painter = painterResource(id = R.drawable.ill_camera_guide),
                    contentDescription = null
                )
            }

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(id = R.string.scan_screen_guide),
                fontSize = 16.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                color = LightAccent
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .align(Alignment.CenterHorizontally),
                onClick = { shouldAnalyse = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MainAccent,
                    contentColor = Background
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 6.dp),
                    text = stringResource(id = R.string.scan_screen_button),
                    fontSize = 18.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }


}

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
class TextRecAnalyser(
    val onCaptureClick: (text: String) -> Unit
) : ImageAnalysis.Analyzer {
    override fun analyze(imageProxy: ImageProxy) {

        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        val mediaImage = imageProxy.image
        mediaImage?.let {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            recognizer.process(image)
                .addOnSuccessListener { textRec ->
                    onCaptureClick(textRec.text)
                }
                .addOnFailureListener {
                    throw Exception("Recognition error: ${it.message}")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
}