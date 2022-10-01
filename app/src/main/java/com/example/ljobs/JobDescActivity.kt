package com.example.ljobs

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.ljobs.Job.JobEntity
import com.example.ljobs.Job.JobViewModel
import com.example.ljobs.JobApplication.JobApplicationDao
import com.example.ljobs.JobApplication.JobApplicationEntity
import com.example.ljobs.JobApplication.JobApplicationViewModel
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.User.UserViewModel
import com.example.ljobs.databinding.ActivityJobDescBinding
import com.example.ljobs.databinding.DialogApplyJobBinding
import kotlinx.coroutines.launch
import kotlin.math.log


class JobDescActivity : AppCompatActivity() {

    private var selectedJobInfo = emptyList<JobEntity>()
    private var _binding: ActivityJobDescBinding? = null
    private val binding get() = _binding!!
    private lateinit var jobViewModel : JobViewModel
    private lateinit var jobApplicationViewModel : JobApplicationViewModel
    lateinit var session : LoginPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this,R.layout.activity_job_desc)
        val view = binding.root
        session = LoginPref(this.applicationContext!!)
        //get current logged in user
        var user:HashMap<String,String> = session.getUserDetails()
        val email = user.get(LoginPref.KEY_EMAIL).toString()
        val userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        jobViewModel = ViewModelProvider(this).get(JobViewModel::class.java)
        jobApplicationViewModel = ViewModelProvider(this).get(JobApplicationViewModel::class.java)


//        val companyName = userViewModel.fetchByEmail(intent.getStringExtra("EMAIL")!!).name
        binding.title.text = intent.getStringExtra("TITLE")
        binding.eduRequirement.text = intent.getStringExtra("EDUREQUIREMENT")
        binding.salary.text = intent.getStringExtra("SALARY")
        binding.location.text = intent.getStringExtra("LOCATION")
        binding.jobDesc.text = intent.getStringExtra("DESCRIPTION")
        binding.companyInfo.text = intent.getStringExtra("COMPANYINFO")

        var item = jobApplicationViewModel.fetchByJobIdAndEmail(intent.getIntExtra("ID", 0),email)

        if(item != null){
            binding.btnApply.text = "WITHDRAW"
            binding.btnApply.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Withdraw Application")
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                builder.setPositiveButton("Yes"){ dialogInterface, _ ->
                    jobApplicationViewModel.deleteApplicationWithJobIdAndEmail(intent.getIntExtra("ID",0), email)
                    Toast.makeText(this,"Withdraw Successfully", Toast.LENGTH_LONG).show()

                    dialogInterface.dismiss()
                }
                builder.setNegativeButton("No"){dialogInterface,_->
                    dialogInterface.dismiss()
                }
                val alertDialog : AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                builder.show()
            }
        }else{
            binding.btnApply.setOnClickListener {
                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_apply_job, null)
                val dialogBuilder = AlertDialog.Builder(this)
                    .setView(dialogView)
                val  jobApplyDialog = dialogBuilder.show()
                jobApplicationViewModel = ViewModelProvider(this).get(JobApplicationViewModel::class.java)
                val bindingJobApply = DialogApplyJobBinding.inflate(layoutInflater)
                jobApplyDialog.setContentView(bindingJobApply.root)

                ArrayAdapter.createFromResource(this@JobDescActivity,R.array.workingExp,android.R.layout.simple_spinner_item).also{
                        adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    bindingJobApply.workingExpSpinner.adapter = adapter
                }

                bindingJobApply.tvApply.setOnClickListener {
                    lifecycleScope.launch{
                        jobApplicationViewModel.insertApplication(JobApplicationEntity(email = email, jobId = intent.getIntExtra("ID", 0), workingExp = bindingJobApply.workingExpSpinner.selectedItem.toString() , additionalInfo = bindingJobApply.tfAdditionalInfo.text.toString(), status = "PENDING"))
                        Toast.makeText(
                            this@JobDescActivity,
                            "Apply Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        jobApplyDialog.dismiss()
                    }
                }

                bindingJobApply.tvCancel.setOnClickListener {
                    jobApplyDialog.dismiss()
                }
            }
        }


        setContentView(view)
    }

}