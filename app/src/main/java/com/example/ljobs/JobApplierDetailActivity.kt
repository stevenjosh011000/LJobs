package com.example.ljobs

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.ljobs.Job.JobViewModel
import com.example.ljobs.User.UserViewModel
import com.example.ljobs.databinding.ActivityJobApplierDetailBinding
import com.example.ljobs.databinding.MyjoblistRecyclerMainBinding



class JobApplierDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobApplierDetailBinding
    private lateinit var mUserViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobApplierDetailBinding.inflate(layoutInflater)
        val view = binding.root

        val intent = getIntent()

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val email:String = intent.getStringExtra("EMAIL").toString()

        val applierData = mUserViewModel.fetchByEmail(email)

        binding.tvApplierName.setText(applierData.name.toString())
        binding.tvApplierEmail.setText(applierData.email.toString())
        binding.tvApplierPhone.append(applierData.phoneNum.toString())
        binding.tvApplierResume.setText(applierData.resume.toString())

        val tel = binding.tvApplierPhone.text.toString()

        binding.tvApplierEmail.setOnClickListener{
            intentMail(email)
        }

        binding.tvApplierPhone.setOnClickListener{
            intentCall(tel)
        }
        setContentView(view)
    }

    private fun intentCall(tel: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data= Uri.parse("tel:$tel")
        startActivity(intent)
    }

    private fun intentMail(email: String) {
        val addresses:Array<String> = arrayOf<String>(email)
        val intent= Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        startActivity(intent)
    }
}