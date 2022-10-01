package com.example.ljobs

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ljobs.User.UserEntity
import com.example.ljobs.User.UserViewModel
import com.example.ljobs.databinding.ActivityUserAddBinding
import java.math.BigInteger
import java.security.MessageDigest

class UserAddActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserAddBinding
    var roleLatest : String? = "1"
    private val URL: String ="http://10.0.2.2/Ljobs/createLan.php"
    private lateinit var mUserViewModel : UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_add)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_add)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setUpActionBar()

        ArrayAdapter.createFromResource(this,R.array.role,android.R.layout.simple_spinner_item).also{
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.roleSpinner.adapter = adapter
        }

        binding.buttonAdd.setOnClickListener {

            if(binding.roleSpinner.selectedItemPosition==0){
                roleLatest = "1"
            }else{
                roleLatest = "2"
            }

            addUser()
        }
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun addUser(){

        val email = binding.emailRg.text.toString()
        val name = binding.nameRg.text.toString()
        val phoneNo = binding.phoneRg.text.toString()
        val password = binding.passwordRg.text.toString()
        val confirmPassword = binding.passwordConRg.text.toString()

        if(email.isEmpty()){
            binding.emailContainerRg.helperText = "Email is required"
            return
        }
        if(!email.matches("^\\S+(?: \\S+)*\$".toRegex())){
            binding.emailContainerRg.helperText = "Cannot start, end with white-space and consist of few white-spaces in a row"
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

        if(!password.matches("^\\S+(?: \\S+)*\$".toRegex())){
            binding.passwordContainerRg.helperText = "Cannot start, end with white-space and consist of few white-spaces in a row"
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
        }
        if(!name.matches("^\\S+(?: \\S+)*\$".toRegex())){
            binding.nameContainerRg.helperText = "Cannot start, end with white-space and consist of few white-spaces in a row"
            return
        }
        else
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

        val emailExist = mUserViewModel.emailExist(email)

        if (emailExist) {
            Toast.makeText(
                this@UserAddActivity,
                "Email has been used",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val stringRequest: StringRequest = object : StringRequest(
                Request.Method.POST, URL,
                Response.Listener { response ->
                    if (response == "success") {
                        mUserViewModel.addUser(
                            UserEntity(
                                email = email,
                                password = md5(password),
                                name = name,
                                phoneNum = phoneNo
                            )
                        )
                        Toast.makeText(
                            this@UserAddActivity,
                            "Account Created Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.emailRg.text?.clear()
                        binding.passwordRg.text?.clear()
                        binding.passwordConRg.text?.clear()
                        binding.nameRg.text?.clear()
                        binding.phoneRg.text?.clear()
                        hideMyKeyboard(binding.phoneRg)
                    } else if (response == "failure") {
                        Toast.makeText(
                            this@UserAddActivity,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("error", error.toString().trim { it <= ' ' })
                    Toast.makeText(
                        applicationContext,
                        error.toString().trim { it <= ' ' },
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String>? {
                    val data: MutableMap<String, String> = HashMap()
                    data["email"] = email!!
                    return data
                }
            }
            val requestQueue = Volley.newRequestQueue(applicationContext)
            requestQueue.add(stringRequest)
        }
    }

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    private fun setUpActionBar(){
        setSupportActionBar(binding.toolbarUserEditActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_back_24)
        }

        binding.toolbarUserEditActivity.setNavigationOnClickListener{onBackPressed()}
    }

    fun hideMyKeyboard(view: View) {
        val hideMe = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        hideMe.hideSoftInputFromWindow(view.windowToken,0)
    }
}