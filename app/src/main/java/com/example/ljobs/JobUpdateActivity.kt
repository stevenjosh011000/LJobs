package com.example.ljobs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.ljobs.Job.JobViewModel
import com.example.ljobs.databinding.ActivityJobUpdateBinding

class JobUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobUpdateBinding
    private lateinit var mJobViewModel: JobViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJobUpdateBinding.inflate(layoutInflater)
        mJobViewModel = ViewModelProvider(this).get(JobViewModel::class.java)
        val view = binding.root
        val intent = getIntent()

        val id: Int = intent.getIntExtra("ID", 0)
        val title:String = intent.getStringExtra("TITLE").toString()
        val eduRequirement:String= intent.getStringExtra("EDUREQUIREMENT").toString()
        val salary:String = intent.getStringExtra("SALARY").toString()
        val description:String = intent.getStringExtra("DESCRIPTION").toString()
        val location:String= intent.getStringExtra("LOCATION").toString()
        val type:String= intent.getStringExtra("TYPE").toString()
        val companyInfo:String= intent.getStringExtra("COMPANYINFO").toString()

        binding.editTextJobTitle.setText(title)
        binding.editTexteduRequire.setText(eduRequirement)
        binding.editTextSalary.setText(salary)
        binding.editTextMultiLineJobDesc.setText(description)
        binding.editTextLocation.setText(location)
        binding.editTextType.setText(type)
        binding.editTextMultiLineCompanyInfo.setText(companyInfo)

        binding.btnUpdate.setOnClickListener {
            updateJob(id)
        }
        binding.btnDelete.setOnClickListener{
            deleteJob(id)
        }
        setContentView(view)
    }

    private fun deleteJob(id: Int) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Job Post ${id}")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes") { _, _ ->
            mJobViewModel.deleteJob(id)
            Toast.makeText(
                this@JobUpdateActivity,
                "Job Post Deleted Successfully",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(this,MyJobPostActivity::class.java)
            startActivity(intent)
        }
        builder.setNegativeButton("No"){_,_,-> }
        builder.create().show()
    }

    private fun updateJob(id: Int) {
        val id:Int=id
        val title: String = binding.editTextJobTitle.text.toString()
        val eduRequire:String=binding.editTexteduRequire.text.toString()
        val salary:String=binding.editTextSalary.text.toString()
        val location:String=binding.editTextLocation.text.toString()
        val jobDesc:String=binding.editTextMultiLineJobDesc.text.toString()
        val jobType:String=binding.editTextType.text.toString()
        val companyInfo:String=binding.editTextMultiLineCompanyInfo.text.toString()

        val titleField = binding.editTextJobTitle
        val eduField=binding.editTexteduRequire
        val salaryField=binding.editTextSalary
        val locationField=binding.editTextLocation
        val typeField=binding.editTextType
        val descField=binding.editTextMultiLineJobDesc
        val compInfoField= binding.editTextMultiLineCompanyInfo

        if (titleField.text.isEmpty()) {
            binding.editTextJobTitle.setError("Job Title cannot be blank")
            return
        }
        if (eduField.text.isEmpty()) {
            binding.editTexteduRequire.setError("Education Requirement cannot be blank")
            return
        }
        if (salaryField.text.isEmpty()) {
            binding.editTextSalary.setError("Salary Range cannot be blank")
            return
        }
        if (locationField.text.isEmpty()) {
            binding.editTextLocation.setError("Location cannot be blank")
            return
        }
        if (typeField.text.isEmpty() ) {
            binding.editTextType.setError("Job Type cannot be blank")
        }
        if (descField.text.isEmpty() ) {
            binding.editTextMultiLineJobDesc.setError("Job Description cannot be blank")
            return
        }
        if (compInfoField.text.isEmpty()) {
            binding.editTextMultiLineCompanyInfo.setError("Company Info cannot be blank")
            return
        }

        if (titleField.text.isNotEmpty() && eduField.text.isNotEmpty() && salaryField.text.isNotEmpty() && locationField.text.isNotEmpty()
            && typeField.text.isNotEmpty() && descField.text.isNotEmpty()
            && compInfoField.text.isNotEmpty()
        ) {

            mJobViewModel.updateJob(title,eduRequire,salary,jobDesc,location,jobType,companyInfo,id)


            Toast.makeText(
                this@JobUpdateActivity,
                "Job Post Updated Successfully",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this,MyJobPostActivity::class.java)
            startActivity(intent)
        }
    }


}