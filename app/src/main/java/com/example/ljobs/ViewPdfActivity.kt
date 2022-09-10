package com.example.ljobs

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import com.github.barteksc.pdfviewer.PDFView
import java.io.File
import java.util.Base64.getDecoder

class ViewPdfActivity : AppCompatActivity() {

    lateinit var pdfView : PDFView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pdf)

        pdfView = findViewById(R.id.pdfView)
        val pdf =intent.getStringExtra("pdf")
        val imageBytes = Base64.decode(pdf, Base64.DEFAULT)
        pdfView.fromBytes(imageBytes).load()

    }
}