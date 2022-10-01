package com.example.ljobs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ljobs.Job.JobViewModel
import com.example.ljobs.databinding.ActivityJobAdListBinding
import com.example.ljobs.databinding.ActivityLoginBinding

class JobAdListActivity : AppCompatActivity() {

    lateinit var binding : ActivityJobAdListBinding
    private lateinit var mJobAdViewModel : JobViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_ad_list)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_job_ad_list)
        setUpActionBar()
        val adapter = JobAdMangeItemAdapter()
        val recyclerView = binding.rvJobList
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        mJobAdViewModel = ViewModelProvider(this).get(JobViewModel::class.java)
        mJobAdViewModel.readAllData.observe(this, Observer { job ->
            adapter.setData(job)
        })

        ArrayAdapter.createFromResource(this,R.array.jobFilter,android.R.layout.simple_spinner_item).also{
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spStatus.adapter = adapter
        }
        binding.buttonFilter.setOnClickListener {
            if(binding.spStatus.selectedItemPosition == 0){
                mJobAdViewModel.readAllData.observe(this, Observer { job ->
                    adapter.setData(job)
                })
            }else if(binding.spStatus.selectedItemPosition == 1){
                mJobAdViewModel.fetchAllByStatus("0").observe(this, Observer { job ->
                    adapter.setData(job)
                })
            }else if(binding.spStatus.selectedItemPosition == 2){
                mJobAdViewModel.fetchAllByStatus("1").observe(this, Observer { job ->
                    adapter.setData(job)
                })
            }else if(binding.spStatus.selectedItemPosition == 3){
                mJobAdViewModel.fetchAllByStatus("2").observe(this, Observer { job ->
                    adapter.setData(job)
                })
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