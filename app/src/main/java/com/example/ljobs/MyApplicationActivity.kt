package com.example.ljobs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ljobs.Job.JobViewModel
import com.example.ljobs.JobApplication.JobApplicationEntity
import com.example.ljobs.JobApplication.JobApplicationViewModel
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.databinding.ActivityMyApplicationBinding

class MyApplicationActivity: AppCompatActivity() {

    private lateinit var binding : ActivityMyApplicationBinding
    private lateinit var jobApplicationViewModel: JobApplicationViewModel
    private lateinit var jobViewModel: JobViewModel
    lateinit var session : LoginPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyApplicationBinding.inflate(layoutInflater)
        val view = binding.root
        session = LoginPref(this.applicationContext!!)
        val jobItemAdapter = JobItemAdapter(JobItemAdapter.SelectJobOnClickListener{ job -> }, this,this)
        val recyclerView = binding.rvJobList
        recyclerView.adapter = jobItemAdapter
        LinearLayoutManager(this).also { recyclerView.layoutManager = it }

        //get current logged in user
        var user:HashMap<String,String> = session.getUserDetails()
        val email = user.get(LoginPref.KEY_EMAIL).toString()

        setUpActionBar()

        jobApplicationViewModel = ViewModelProvider(this).get(JobApplicationViewModel::class.java)
        jobViewModel = ViewModelProvider(this).get(JobViewModel::class.java)

        jobViewModel.fetchByApplicationEmail(email).observe(this) {
            jobItemAdapter.setJobList(it)
            binding.rvJobList.layoutManager!!.scrollToPosition(it.size-1)
        }

        val llm = LinearLayoutManager(this.applicationContext)
        llm.stackFromEnd = true
        binding.rvJobList.layoutManager = llm
        binding.rvJobList.adapter = jobItemAdapter

        setContentView(view)
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