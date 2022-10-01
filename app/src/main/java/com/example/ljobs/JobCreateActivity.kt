package com.example.ljobs

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.ljobs.Job.JobEntity
import com.example.ljobs.Job.JobViewModel
import com.example.ljobs.User.UserViewModel
import com.example.ljobs.databinding.ActivityJobCreateBinding
import com.example.ljobs.Session.LoginPref

class JobCreateActivity : AppCompatActivity() {

    lateinit var binding: ActivityJobCreateBinding
    lateinit var session : LoginPref
    private lateinit var mJobViewModel: JobViewModel
    var email: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_create)
        mJobViewModel = ViewModelProvider(this).get(JobViewModel::class.java)

        session = LoginPref(this.applicationContext!!)
        binding.buttonCreate.setOnClickListener {
            addJob()
        }



    }

    private fun addJob() {
        val jobTitle = binding.editTextJobTitle.text.toString()
        val eduRequire = binding.editTexteduRequire.text.toString()
        val salary = binding.editTextSalary.text.toString()
        val location = binding.editTextLocation.text.toString()
        val jobType = binding.editTextType.text.toString()
        val jobDesc = binding.editTextMultiLineJobDesc.text.toString()
        val companyInfo = binding.editTextMultiLineCompanyInfo.text.toString()

        var user:HashMap<String,String> = session.getUserDetails()
        email = user.get(LoginPref.KEY_EMAIL).toString()

        if (jobTitle.isEmpty()) {
            binding.editTextJobTitle.setError("Job Title cannot be blank")
            return
        }
        if (eduRequire.isEmpty()) {
            binding.editTexteduRequire.setError("Education Requirement cannot be blank")
            return
        }
        if (salary.isEmpty()) {
            binding.editTextSalary.setError("Salary Range cannot be blank")
            return
        }
        if (location.isEmpty()) {
            binding.editTextLocation.setError("Location cannot be blank")
            return
        }
        if (jobType.isEmpty()) {
            binding.editTextType.setError("Job Type cannot be blank")
        }
        if (jobDesc.isEmpty()) {
            binding.editTextMultiLineJobDesc.setError("Job Description cannot be blank")
            return
        }
        if (companyInfo.isEmpty()) {
            binding.editTextMultiLineCompanyInfo.setError("Company Info cannot be blank")
            return
        }

        if (jobTitle.isNotEmpty() && eduRequire.isNotEmpty() && salary.isNotEmpty() && location.isNotEmpty() && jobType.isNotEmpty() && jobDesc.isNotEmpty()
            && companyInfo.isNotEmpty()
        ) {
            mJobViewModel.addJob(
                JobEntity(
                    email = email,
                    title = jobTitle,
                    eduRequirement = eduRequire,
                    salary = salary,
                    descrip = jobDesc,
                    location = location,
                    type = jobType,
                    companyInfo = companyInfo,
                    jobStatus = "Pending"
                )
            )
            Toast.makeText(
                this@JobCreateActivity,
                "Job Post Created Successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}