package com.example.ljobs

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.ljobs.Edu.EduEntity
import com.example.ljobs.Job.JobViewModel
import com.example.ljobs.databinding.ActivityJobAdEditBinding
import com.example.ljobs.databinding.ActivityJobAdManageBinding
import kotlinx.coroutines.launch

class JobAdManageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobAdManageBinding
    private lateinit var mJobViewModel : JobViewModel
    var jobId : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_ad_manage)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_job_ad_manage)
        mJobViewModel = ViewModelProvider(this).get(JobViewModel::class.java)
        setUpActionBar()
        val intent = intent
        val id = intent.getStringExtra("id")
        jobId = id.toString().toInt()

        val job = mJobViewModel.fetchJobById(id.toString().toInt())
        if(job!=null){
            binding.tvJobTitle.text = job.title
            binding.tveduRequire.text = job.eduRequirement
            binding.tvLocation.text = job.location
            binding.tvSalary.text = job.salary
            binding.tvType.text = job.type
            binding.tvMultiLineCompanyInfo.text = job.companyInfo
            binding.tvMultiLineJobDesc.text = job.desc
            if(job.jobStatus == "0"){
                binding.tvStatus.text = "PENDING"
                binding.buttonApprove.visibility = View.VISIBLE
                binding.buttonReject.visibility = View.VISIBLE
                binding.buttonDelete.visibility = View.VISIBLE
                binding.buttonDelist.visibility = View.GONE
            }else if(job.jobStatus == "1"){
                binding.tvStatus.text = "APPROVED"
                binding.buttonApprove.visibility = View.GONE
                binding.buttonReject.visibility = View.GONE
                binding.buttonDelete.visibility = View.VISIBLE
                binding.buttonDelist.visibility = View.VISIBLE
            }else if(job.jobStatus == "2"){
                binding.tvStatus.text = "REJECTED"
                binding.buttonApprove.visibility = View.VISIBLE
                binding.buttonReject.visibility = View.GONE
                binding.buttonDelete.visibility = View.VISIBLE
                binding.buttonDelist.visibility = View.GONE
            }else{
                binding.tvStatus.text = "-"
            }
        }

        binding.buttonApprove.setOnClickListener {

            mJobViewModel.updateJobStatus("1",jobId)
            updateStatus("1")
        }
        binding.buttonDelist.setOnClickListener {
            mJobViewModel.updateJobStatus("0",jobId)
            updateStatus("0")
        }
        binding.buttonReject.setOnClickListener {
            mJobViewModel.updateJobStatus("2",jobId)
            updateStatus("2")
        }

        binding.buttonDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            builder.setTitle("Confirm delete Job Ad?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("Yes"){
                    dialogInterface, _ ->
                mJobViewModel.deleteJobAd(id.toString().toInt())
                Toast.makeText(this,"Job ad deleted successful", Toast.LENGTH_SHORT).show()
                dialogInterface.dismiss()
                this.finish()
            }
            builder.setNegativeButton("No"){dialogInterface,_->
                dialogInterface.dismiss()
            }

            val alertDialog : AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            builder.show()
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

    private fun updateStatus(status :String){
        if(status == "0"){
            binding.tvStatus.text = "PENDING"
            binding.buttonApprove.visibility = View.VISIBLE
            binding.buttonReject.visibility = View.VISIBLE
            binding.buttonDelete.visibility = View.VISIBLE
            binding.buttonDelist.visibility = View.GONE
        }else if(status == "1"){
            binding.tvStatus.text = "APPROVED"
            binding.buttonApprove.visibility = View.GONE
            binding.buttonReject.visibility = View.GONE
            binding.buttonDelete.visibility = View.VISIBLE
            binding.buttonDelist.visibility = View.VISIBLE
        }else if(status == "2"){
            binding.tvStatus.text = "REJECTED"
            binding.buttonApprove.visibility = View.VISIBLE
            binding.buttonReject.visibility = View.GONE
            binding.buttonDelete.visibility = View.VISIBLE
            binding.buttonDelist.visibility = View.GONE
        }else{
            binding.tvStatus.text = "-"
        }
    }
}