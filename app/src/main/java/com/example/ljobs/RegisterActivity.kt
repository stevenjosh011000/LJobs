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
import java.math.BigInteger
import java.security.MessageDigest


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
        val name = binding.nameRg.text.toString()
        val phoneNo = binding.phoneRg.text.toString()


        if(email.isEmpty()){
            binding.emailContainerRg.helperText = "Email is required"
            return
        }

        if(!isValidEmail(email)){
            binding.emailContainerRg.helperText = "Invalid email"
            return

        }else{
            binding.emailContainerRg.helperText = ""
        }

        if(password.isEmpty()){
            binding.passwordContainerRg.helperText = "Password is required"
            return
        }

        if(password.length<6){
            binding.passwordContainerRg.helperText = "Minimum 6 character password"
            return
        }

        if(!password.matches(".*[A-Z].*".toRegex())){
            binding.passwordContainerRg.helperText = "Must contain 1 upper-case character"
            return
        }

        if(!password.matches(".*[a-z].*".toRegex())){
            binding.passwordContainerRg.helperText = "Must contain 1 lower-case character"
            return
        }

        if(!password.matches(".*[@#\$%^&+=].*".toRegex())){
            binding.passwordContainerRg.helperText = "Must contain 1 special character"
            return

        }else{
            binding.passwordContainerRg.helperText = ""
        }

        if(confirmPassword.isEmpty()){
            binding.confirmPassContainerRg.helperText = "Confirm password is required"
            return
        }

        if(password != confirmPassword){
            binding.confirmPassContainerRg.helperText = "Confirm password does not match the password"
            return
        }else{
            binding.confirmPassContainerRg.helperText = ""
        }

        if(name.isEmpty()){
            binding.nameContainerRg.helperText = "Name is required"
            return
        }

        if(name.length<2) {
            binding.nameContainerRg.helperText = "Minimum 3 character"
            return
        }else
        {
            binding.nameContainerRg.helperText = ""
        }

        if(phoneNo.isEmpty()){
            binding.phoneContainerRg.helperText = "Phone number is required"
            return
        }

        if(phoneNo.length < 9){
            binding.phoneContainerRg.helperText = "Phone Number : 9-10 digits"
            return
        }

        if(phoneNo.length > 10){
            binding.phoneContainerRg.helperText = "Phone Number : 9-10 digits"
            return
        }
        else{
            binding.phoneContainerRg.helperText = ""
        }


        lifecycleScope.launch{

            if(userDao.isEmailExist(email)){
                Toast.makeText(
                    this@RegisterActivity,
                    "Email has been used",
                    Toast.LENGTH_SHORT
                ).show()
            }else {

                userDao.insert(UserEntity(email = email, password = md5(password), name = name, phoneNum = phoneNo))
                Toast.makeText(
                    this@RegisterActivity,
                    "Account Created Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                binding.emailRg.text?.clear()
                binding.passwordRg.text?.clear()
                binding.passwordConRg.text?.clear()
                binding.nameRg.text?.clear()
                binding.phoneRg.text?.clear()

            }


        }

    }

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
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