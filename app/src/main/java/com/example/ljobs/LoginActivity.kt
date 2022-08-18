package com.example.ljobs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.ljobs.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this , R.layout.activity_login)

        setUpActionBar()

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }

    }

    private fun setUpActionBar(){
        setSupportActionBar(binding.toolbarLoginActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_back_24)
        }

        binding.toolbarLoginActivity.setNavigationOnClickListener{onBackPressed()}
    }
}