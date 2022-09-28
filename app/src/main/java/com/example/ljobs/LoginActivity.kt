package com.example.ljobs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.User.UserDao
import com.example.ljobs.User.UserEntity
import com.example.ljobs.User.UserViewModel
import com.example.ljobs.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest


class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    lateinit var session : LoginPref
    private lateinit var mUserViewModel : UserViewModel
    private val URL: String ="http://10.0.2.2/Ljobs/checkConnection.php"

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



        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, URL,
            Response.Listener { response ->
                if (response == "success") {
                    val account = mUserViewModel.fetchByEmail(email)
                    if (account != null){
                        if(account.password == md5(binding.passwordLg.text.toString())){
                            session.createLoginSession(account.id.toString(),account.email!!,account.password!!,account.resume.toString(),account.resumeName.toString(),account.role.toString())
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
                }else{
                    Toast.makeText(
                        this,
                        "Connection failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            Response.ErrorListener { error ->
//                Log.e("error", error.toString().trim { it <= ' ' })
                Toast.makeText(
                    this,
                    "Connection failed",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                val data: MutableMap<String, String> = HashMap()
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(applicationContext)
        requestQueue.add(stringRequest)

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