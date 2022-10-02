package com.example.ljobs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ljobs.JobApplication.JobApplicationViewModel
import com.example.ljobs.databinding.ActivityMyJobApplierListMainBinding
import androidx.lifecycle.Observer
import com.example.ljobs.JobApplication.JobApplicationEntity

class MyJobApplierList : AppCompatActivity() {

    private lateinit var binding: ActivityMyJobApplierListMainBinding
    private lateinit var mJobApplicationViewModel: JobApplicationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyJobApplierListMainBinding.inflate(layoutInflater)
        val view = binding.root

        val intent= getIntent()
        var id: Int = intent.getIntExtra("JobID", 0).toInt()


        val adapter = JobApplierAdapter()
        mJobApplicationViewModel = ViewModelProvider(this).get(JobApplicationViewModel::class.java)
        val recyclerView = binding.rvApplierList
        recyclerView.adapter=adapter
        LinearLayoutManager(this).also { recyclerView.layoutManager = it }

        setUpActionBar()

        mJobApplicationViewModel = ViewModelProvider(this).get(JobApplicationViewModel::class.java)

        mJobApplicationViewModel.fetchByJobId(id).observe(this, Observer { jobApplier-> adapter.setData(jobApplier as ArrayList<JobApplicationEntity>) })


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