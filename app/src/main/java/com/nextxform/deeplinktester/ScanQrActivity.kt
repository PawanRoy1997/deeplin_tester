package com.nextxform.deeplinktester

import android.content.Intent
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class ScanQrActivity : CaptureActivity() {
    override fun initializeContent(): DecoratedBarcodeView {
        setContentView(R.layout.capture_qr_code)
        return findViewById(R.id.scanView)
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(Intents.Scan.RESULT, "{}")
        intent.putExtra("CANCELLED", true)
        intent.putExtra(Intents.Scan.RESULT_FORMAT, BarcodeFormat.QR_CODE)
        intent.putExtra(Intents.Scan.RESULT_BYTES, byteArrayOf())
        intent.putExtra(Intents.Scan.RESULT_ORIENTATION, Integer.MIN_VALUE)
        intent.putExtra(Intents.Scan.RESULT_ERROR_CORRECTION_LEVEL, "")
        intent.putExtra(Intents.Scan.RESULT_BARCODE_IMAGE_PATH, "")

        setResult(RESULT_CANCELED, intent)

        finish()
    }
}