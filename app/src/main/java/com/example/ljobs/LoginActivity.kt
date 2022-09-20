package com.example.ljobs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.User.UserDao
import com.example.ljobs.User.UserViewModel
import com.example.ljobs.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest


class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    lateinit var session : LoginPref
    private lateinit var mUserViewModel : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this , R.layout.activity_login)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setUpActionBar()

        binding.btnLogin.setOnClickListener {
            login(binding.emailLg.text.toString())
        }

        session = LoginPref(this)




    }

    private fun login(email:String){

        val account = mUserViewModel.fetchByEmail(email)
//        lifecycleScope.launch{
//            userDao.fetchUserByEmail(email).collect{
                if (account != null){
                    if(account.password == md5(binding.passwordLg.text.toString())){
                        session.createLoginSession(account.id.toString(),account.email!!,account.password!!,account.resume.toString(),account.resumeName.toString())
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
                }else{
                    Toast.makeText(
                        this@LoginActivity,
                        "Invalid Email or Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
//            }
//        }

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