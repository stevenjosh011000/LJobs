package com.example.ljobs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.User.UserEntity
import com.example.ljobs.User.UserViewModel
import com.example.ljobs.databinding.ActivityFeedbackBinding
import com.example.ljobs.databinding.ActivityIntroBinding

class FeedbackActivity : AppCompatActivity() {

    lateinit var session : LoginPref
    lateinit var binding : ActivityFeedbackBinding
    private lateinit var mUserViewModel : UserViewModel
    private val URL: String ="http://10.0.2.2/Ljobs/createFeedBack.php"
    var email: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        session = LoginPref(this.applicationContext)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_feedback)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setUpActionBar()
        var user:HashMap<String,String> = session.getUserDetails()
        email = user.get(LoginPref.KEY_EMAIL).toString()

        val account = mUserViewModel.fetchByEmail(email!!)
        if (account != null) {
            binding.tvEmail.text = account.email
            binding.tvName.text = account.name
            binding.tvPhoneNum.text = account.phoneNum
        }

        binding.buttonSubmit.setOnClickListener {
            if (account != null) {
                val stringRequest: StringRequest = object : StringRequest(
                    Request.Method.POST, URL,
                    Response.Listener { response ->
                        if (response == "success") {
                            Toast.makeText(
                                this@FeedbackActivity,
                                "Feedback Submitted Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.description.text.clear()
                            this@FeedbackActivity.finish()
                        } else if (response == "failure") {
                            Toast.makeText(
                                this@FeedbackActivity,
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
                        data["name"] = account.name!!
                        data["phoneNum"] = account.phoneNum!!
                        data["description"] = binding.description.text.toString()
                        return data
                    }
                }
                val requestQueue = Volley.newRequestQueue(applicationContext)
                requestQueue.add(stringRequest)
            }
        }
    }
    private fun setUpActionBar(){
        setSupportActionBar(binding.toolbarJobAdListActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_back_24)
        }

        binding.toolbarJobAdListActivity.setNavigationOnClickListener{onBackPressed()}
    }
}