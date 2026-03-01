package com.nextxform.deeplinktester.viewModels

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateQRViewModel @Inject constructor() : ViewModel() {
    private val writer = QRCodeWriter()
    private val _isGenerating: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating
    var qrCode by mutableStateOf(value = createBitmap(1000, 1000))

    fun generateQRof(link: String, background: Drawable?, onFailure: (String) -> Unit) {
        if(isGenerating.value) return
        viewModelScope.launch {
            _isGenerating.emit(true)
            try {
                val width = 1000
                val height = 1000
                val bitMatrix = writer.encode(link, BarcodeFormat.QR_CODE, 32, 32)

                val bitmap = createBitmap(width, height, Bitmap.Config.RGB_565)

                val canvas = Canvas(bitmap).apply { drawColor(Color.WHITE) }
                val cellSize = (canvas.width - 2).toFloat() / bitMatrix.width
                val blackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { setColor(Color.BLACK) }

                background?.let { bg ->
                    val padding = (0.1f * canvas.width).toInt()
                    bg.setBounds(padding, padding, canvas.width - padding, canvas.height - padding)
                    bg.alpha = 100
                    bg.draw(canvas)
                }

                repeat(bitMatrix.width) { i ->
                    repeat(bitMatrix.height) { j ->
                        if (bitMatrix[i, j]) {
                            canvas.drawCircle((i * cellSize) + cellSize, (j * cellSize) + cellSize, cellSize / 3f, blackPaint)
                        }
                    }
                }

                qrCode = bitmap
            } catch (e: Exception) {
                onFailure.invoke(e.message.orEmpty())
            } finally {
                _isGenerating.emit(false)
            }
        }
    }
}

class PreviewGenerateQRViewModel(): PreviewParameterProvider<GenerateQRViewModel>{
    override val values: Sequence<GenerateQRViewModel>
        get() = sequenceOf(GenerateQRViewModel())

}