package com.example.ljobs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.ljobs.Job.JobEntity
import com.example.ljobs.Job.JobViewModel
import com.example.ljobs.User.UserViewModel
import com.example.ljobs.databinding.ActivityJobAdAddBinding
import com.example.ljobs.databinding.ActivityJobAdListBinding

class JobAdAddActivity : AppCompatActivity() {

    lateinit var binding : ActivityJobAdAddBinding
    private lateinit var mJobViewModel : JobViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_ad_add)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_job_ad_add)
        mJobViewModel = ViewModelProvider(this).get(JobViewModel::class.java)

        setUpActionBar()

        binding.button2.setOnClickListener {
            if(binding.editTextJobTitle.text.toString()!="" && binding.editTextLocation.text.toString() !="" && binding.editTexteduRequire.text.toString()!="" && binding.editTextSalary.text.toString()!="" && binding.editTextMultiLineJobDesc.text.toString() != "" && binding.editTextType.text.toString()!="" && binding.editTextMultiLineCompanyInfo.text.toString()!=""){
                mJobViewModel.addJob(JobEntity( title = binding.editTextJobTitle.text.toString(), eduRequirement = binding.editTexteduRequire.text.toString(), salary = binding.editTextSalary.text.toString(), desc = binding.editTextMultiLineJobDesc.text.toString(), type = binding.editTextType.text.toString(), location = binding.editTextLocation.text.toString(), companyInfo = binding.editTextMultiLineCompanyInfo.text.toString(), jobStatus = "1"))

                binding.editTextJobTitle.text.clear()
                binding.editTexteduRequire.text.clear()
                binding.editTextSalary.text.clear()
                binding.editTextMultiLineJobDesc.text.clear()
                binding.editTextType.text.clear()
                binding.editTextLocation.text.clear()
                binding.editTextMultiLineCompanyInfo.text.clear()
                Toast.makeText(this@JobAdAddActivity,"Inserted Successfully",Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(this@JobAdAddActivity,"Please fill out all field",Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setUpActionBar(){
        setSupportActionBar(binding.toolbarJobAdAddActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_back_24)
        }

        binding.toolbarJobAdAddActivity.setNavigationOnClickListener{onBackPressed()}
    }
}