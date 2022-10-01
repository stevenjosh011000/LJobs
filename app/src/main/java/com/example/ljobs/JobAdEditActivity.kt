package com.example.ljobs

import android.R.id
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.ljobs.Job.JobEntity
import com.example.ljobs.Job.JobViewModel
import com.example.ljobs.databinding.ActivityJobAdEditBinding


class JobAdEditActivity : AppCompatActivity() {

    private lateinit var binding:ActivityJobAdEditBinding
    private lateinit var mJobViewModel : JobViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_ad_edit)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_job_ad_edit)
        mJobViewModel = ViewModelProvider(this).get(JobViewModel::class.java)
        setUpActionBar()

        val intent = intent
        val id = intent.getStringExtra("id")

        val job = mJobViewModel.fetchJobEntityById(id.toString().toInt())
        if(job!=null){
            binding.editTextJobTitle.setText(job.title)
            binding.editTexteduRequire.setText(job.eduRequirement)
            binding.editTextSalary.setText(job.salary)
            binding.editTextMultiLineJobDesc.setText(job.desc)
            binding.editTextType.setText(job.type)
            binding.editTextLocation.setText(job.location)
            binding.editTextMultiLineCompanyInfo.setText(job.companyInfo)
        }
        binding.buttonEdit.setOnClickListener {
            if(binding.editTextJobTitle.text.toString()!="" && binding.editTextLocation.text.toString() !="" && binding.editTexteduRequire.text.toString()!="" && binding.editTextSalary.text.toString()!="" && binding.editTextMultiLineJobDesc.text.toString() != "" && binding.editTextType.text.toString()!="" && binding.editTextMultiLineCompanyInfo.text.toString()!=""){
                mJobViewModel.updateJobAd(title = binding.editTextJobTitle.text.toString(), eduRequirement = binding.editTexteduRequire.text.toString(), salary = binding.editTextSalary.text.toString(), desc = binding.editTextMultiLineJobDesc.text.toString(), type = binding.editTextType.text.toString(), location = binding.editTextLocation.text.toString(), companyInfo = binding.editTextMultiLineCompanyInfo.text.toString(), jobStatus = job.jobStatus.toString(), id = id.toString().toInt())
                Toast.makeText(this@JobAdEditActivity,"Updated Successfully",Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(this@JobAdEditActivity,"Please fill out all field",Toast.LENGTH_SHORT).show()
            }
        }


    }
    private fun setUpActionBar(){
        setSupportActionBar(binding.toolbarJobAdEditActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_back_24)
        }

        binding.toolbarJobAdEditActivity.setNavigationOnClickListener{onBackPressed()}
    }
}