package com.example.ljobs

import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.example.ljobs.Session.LoginPref
import com.github.barteksc.pdfviewer.PDFView


class ViewPdfActivity : AppCompatActivity() {

    lateinit var pdfView : PDFView
    lateinit var session : LoginPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pdf)
        session = LoginPref(this)
        pdfView = findViewById(R.id.pdfView)

        var user:HashMap<String,String> = session.getUserDetails()

        val imageBytes = Base64.decode(user.get(LoginPref.RESUME).toString(), Base64.DEFAULT)
        pdfView.fromBytes(imageBytes).load()

    }
}