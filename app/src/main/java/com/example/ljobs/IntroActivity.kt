package com.example.ljobs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ljobs.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private var binding : ActivityIntroBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnSignUp?.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        binding?.btnSignInIntro?.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

    }
}