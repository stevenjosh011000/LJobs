package com.example.ljobs

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest


class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    lateinit var session : LoginPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this , R.layout.activity_login)

        setUpActionBar()

        val userDao = (application as UserApp).db.userDao()

        binding.btnLogin.setOnClickListener {
            login(binding.emailLg.text.toString(),userDao)
        }

        session = LoginPref(this)




    }

    private fun login(email:String,userDao: UserDao){

        lifecycleScope.launch{
            userDao.fetchUserByEmail(email).collect{
                if (it!=null){
                    if(it.password == md5(binding.passwordLg.text.toString())){
                        session.createLoginSession(it.id.toString(),it.email!!,it.password)
                        val intent = Intent(this@LoginActivity,HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }else{
                        Toast.makeText(
                            this@LoginActivity,
                            "Invalid Email or Password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
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