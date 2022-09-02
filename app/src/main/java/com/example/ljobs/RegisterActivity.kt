package com.example.ljobs

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.ljobs.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch


class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register)

        setUpActionBar()
        val userDao = (application as UserApp).db.userDao()

        binding.btnSignUp.setOnClickListener {
            addUser(userDao)
        }




    }

    private fun addUser(userDao: UserDao){

        val email = binding.emailRg.text.toString()
        val password = binding.passwordRg.text.toString()
        val confirmPassword = binding.passwordConRg.text.toString()


        if(email.isEmpty()){
            Toast.makeText(this@RegisterActivity,"Email cannot be blank", Toast.LENGTH_SHORT).show()
            return
        }

        if(password.isEmpty()){
            Toast.makeText(this@RegisterActivity,"Password cannot be blank", Toast.LENGTH_SHORT).show()
            return
        }

        if(confirmPassword.isEmpty()){
            Toast.makeText(this@RegisterActivity,"Confirm password cannot be blank", Toast.LENGTH_SHORT).show()
            return
        }

        if(!isValidEmail(email)){
            Toast.makeText(this@RegisterActivity,"It's not a valid email", Toast.LENGTH_SHORT).show()
            return
        }

        if(password != confirmPassword){
            Toast.makeText(this@RegisterActivity,"Confirm password does not match the password", Toast.LENGTH_SHORT).show()
            return
        }

        val longValue = Long.MAX_VALUE
        lifecycleScope.launch{
            userDao.insert(UserEntity(email = email, password = password))
            Toast.makeText(this@RegisterActivity,"Account Created Successfully", Toast.LENGTH_SHORT).show()
            binding.emailRg.text?.clear()
            binding.passwordRg.text?.clear()
            binding.passwordConRg.text?.clear()
        }

    }

    private fun setUpActionBar(){
        setSupportActionBar(binding.toolbarSignUpActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_back_24)
        }

        binding.toolbarSignUpActivity.setNavigationOnClickListener{onBackPressed()}
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}