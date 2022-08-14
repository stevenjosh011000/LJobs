package com.example.ljobs

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ljobs.databinding.ActivitySplashBinding
import org.w3c.dom.Text


class SplashActivity : AppCompatActivity() {

    private var binding : ActivitySplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val typeFace: Typeface = Typeface.createFromAsset(assets,"goodTimesRg.otf")
        val title:TextView = findViewById(R.id.splashTitle)

        val paint = title.paint
        val width = paint.measureText(title.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width, title.textSize, intArrayOf(
            Color.parseColor("#1976D2"),
            Color.parseColor("#1976D2"),
            /*Color.parseColor("#8446CC"),
            Color.parseColor("#ff1a1a"),*/
            Color.parseColor("#ff1a1a")
        ), null, Shader.TileMode.REPEAT)

        title.paint.setShader(textShader)
        binding?.splashTitle?.typeface = typeFace;




        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,IntroActivity::class.java))
            finish()
        },2500)
    }
}