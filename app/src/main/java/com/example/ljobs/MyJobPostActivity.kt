package com.example.ljobs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ljobs.Job.JobEntity
import com.example.ljobs.Job.JobViewModel
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.databinding.MyjoblistRecyclerMainBinding

class MyJobPostActivity : AppCompatActivity() {

    private lateinit var binding: MyjoblistRecyclerMainBinding
    private lateinit var mJobViewModel: JobViewModel
    lateinit var session : LoginPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyjoblistRecyclerMainBinding.inflate(layoutInflater)
        val view = binding.root
        session = LoginPref(this.applicationContext!!)
        val adapter = JobListAdapter()
        mJobViewModel = ViewModelProvider(this).get(JobViewModel::class.java)
        val recyclerView = binding.rvJobList
        recyclerView.adapter=adapter
        LinearLayoutManager(this).also { recyclerView.layoutManager = it }

        //get current logged in user
        var user:HashMap<String,String> = session.getUserDetails()
        val email = user.get(LoginPref.KEY_EMAIL).toString()

        mJobViewModel = ViewModelProvider(this).get(JobViewModel::class.java)
        mJobViewModel.fetchByEmail(email).observe(this, Observer { job-> adapter.setData(job as ArrayList<JobEntity>) })
        /* mJobViewModel.readAllData.observe(this, Observer { job-> adapter.setData(job as ArrayList<JobEntity>) })
     */
        setContentView(view)
    }


}