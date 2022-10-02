package com.example.ljobs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import androidx.lifecycle.ViewModelProvider
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.User.UserViewModel
import com.github.barteksc.pdfviewer.PDFView

class JobApplierDetailResumeActivity : AppCompatActivity() {
    lateinit var pdfView : PDFView
    lateinit var mUserViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_applier_detail_resume)
        pdfView = findViewById(R.id.pdfView)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val intent = getIntent()
        val email = intent.getStringExtra("EMAIL")


        val imageBytes = Base64.decode(mUserViewModel.fetchByEmail(email!!).resume.toString(), Base64.DEFAULT)
        pdfView.fromBytes(imageBytes).load()


    }

}