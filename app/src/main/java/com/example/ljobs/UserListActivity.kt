package com.example.ljobs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ljobs.Job.JobViewModel
import com.example.ljobs.User.UserViewModel
import com.example.ljobs.databinding.ActivityUserListBinding

class UserListActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserListBinding
    private lateinit var mUserViewModel : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_list)
        setUpActionBar()
        val adapter = userMangeItemAdapter()
        val recyclerView = binding.rvUserList
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        mUserViewModel.readAllData.observe(this, Observer { user ->
            adapter.setData(user)
        })

        binding.add.setOnClickListener{
            val intent = Intent(this, UserAddActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setUpActionBar(){
        setSupportActionBar(binding.toolbarUserListActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_back_24)
        }

        binding.toolbarUserListActivity.setNavigationOnClickListener{onBackPressed()}
    }
}