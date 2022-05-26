package se.joelbit.dialife.experimental

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.flow
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@Composable
fun CameraView(
    context: Context,
    outdir: File,
    executor: Executor,
    onError: (ImageCaptureException) -> Unit,
    onSuccess: (Uri) -> Unit,
){
   val lensdir = CameraSelector.LENS_FACING_BACK
//   val context = LocalContext.current
    val lco = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val pvv = remember { PreviewView(context) }
    val imgCapt = remember { ImageCapture.Builder().build() }
    val camSel = CameraSelector.Builder()
        .requireLensFacing(lensdir)
        .build()

    LaunchedEffect(key1 = lensdir){
        with(context.getCameraProvider()){
            unbindAll()
            bindToLifecycle(
                lco,camSel,preview,imgCapt
            )
        }

        preview.setSurfaceProvider(pvv.surfaceProvider)

    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { pvv }, modifier = Modifier.fillMaxSize())
        
        IconButton(onClick = {
            imgCapt.takePhoto(
                outdir = outdir,
                executor = executor,
                onError = onError,
                onSuccess = onSuccess
            )
        }) {
            Icon(
                imageVector = Icons.Sharp.Lens,
                contentDescription = "Picture taken",
                tint = Color.Green,
                modifier = Modifier
                    .size(100.dp)
                    .padding(1.dp)
                    .border(1.dp, Color.Blue, CircleShape)
            )
        }
    }
}

fun ImageCapture.takePhoto(
    filePattern: String = "yyyy-MM-dd HH-mm-ss-SSS",
    outdir: File,
    executor: Executor,
    onError: (ImageCaptureException) -> Unit,
    onSuccess: (Uri) -> Unit
    ) {
    val file = File(
        outdir,
        SimpleDateFormat(filePattern, Locale.getDefault())
            .format(System.currentTimeMillis())+".jpg")

    val outOpts = ImageCapture.OutputFileOptions.Builder(file).build()

    takePicture(outOpts, executor, object : ImageCapture.OnImageSavedCallback{
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            Log.d("Camera", "${outputFileResults.savedUri} == $file")
            onSuccess(outputFileResults.savedUri!!)
        }

        override fun onError(exception: ImageCaptureException) {
            onError(exception)
        }

    })
}

suspend fun Context.getCameraProvider() = suspendCoroutine<ProcessCameraProvider> { continuation ->
    ProcessCameraProvider.getInstance(this).also { provider ->
        provider.addListener(
            {continuation.resume(provider.get())},
            ContextCompat.getMainExecutor(this))
    }

}