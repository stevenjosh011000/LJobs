package com.example.ljobs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ljobs.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private var binding: ActivityRegisterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpActionBar()
    }

    private fun setUpActionBar(){
        setSupportActionBar(binding?.toolbarSignUpActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_back_24)
        }

        binding?.toolbarSignUpActivity?.setNavigationOnClickListener{onBackPressed()}
    }
}