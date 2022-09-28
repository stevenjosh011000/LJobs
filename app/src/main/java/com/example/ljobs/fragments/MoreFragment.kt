package com.example.ljobs.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.ljobs.*
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.databinding.FragmentMoreBinding


class MoreFragment : Fragment() {

    lateinit var session : LoginPref
    var role: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = LoginPref(activity?.applicationContext!!)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentMoreBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_more, container, false)
        var user:HashMap<String,String> = session.getUserDetails()
        role = user.get(LoginPref.ROLE).toString()
        if(role.toString() == "2" || role.toString() == "3"){
            binding.tvManageJobAd.visibility = View.VISIBLE
            binding.tvAddJobAd.visibility = View.VISIBLE
            binding.tvManageUser.visibility = View.VISIBLE
        }

        binding.tvAddJobAd.setOnClickListener {
            val intent = Intent(context, JobAdAddActivity::class.java)
            startActivity(intent)
        }

        binding.tvManageJobAd.setOnClickListener {
            val intent = Intent(context, JobAdListActivity::class.java)
            startActivity(intent)
        }

        binding.tvManageUser.setOnClickListener {
            val intent = Intent(context, UserListActivity::class.java)
            startActivity(intent)
        }

        binding.tvFeedback.setOnClickListener {
            val intent = Intent(context,  FeedbackActivity::class.java)
            startActivity(intent)
        }

        binding.tvSignOut.setOnClickListener{
            session.logoutUser()
        }


        return binding.root
    }
}