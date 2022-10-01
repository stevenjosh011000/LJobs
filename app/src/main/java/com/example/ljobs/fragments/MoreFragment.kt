package com.example.ljobs.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ljobs.Job.JobViewModel
import com.example.ljobs.JobCreateActivity
import com.example.ljobs.MyApplicationActivity
import com.example.ljobs.MyJobPostActivity
import com.example.ljobs.R
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.databinding.FragmentMoreBinding


class MoreFragment : Fragment() {

    lateinit var session : LoginPref

    lateinit var email: String
    private lateinit var mJobViewModel : JobViewModel
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

        binding.tvSignOut.setOnClickListener{
            session.logoutUser()
        }

        binding.tvCreateJob.setOnClickListener {
            val intent = Intent(getActivity(),JobCreateActivity::class.java)
            getActivity()?.startActivity(intent)
        }
//Set the visibility of viewing job post
        //If user haven't create any job post, then the "My Job Post" would not appear
        var user:HashMap<String,String> = session.getUserDetails()
        mJobViewModel = ViewModelProvider(this).get(JobViewModel::class.java)
        email = user.get(LoginPref.KEY_EMAIL).toString()

        val isJobPostExist = mJobViewModel.emailExist(email)
        if(isJobPostExist){
            binding.tvMyJobPost.visibility = View.VISIBLE
        }
        else{
            binding.tvMyJobPost.visibility = View.GONE

        }
        binding.tvMyJobPost.setOnClickListener {
            val intent = Intent(requireActivity(),MyJobPostActivity::class.java)
            Log.d("MoreFragment48",  intent.toString())
            requireActivity()
                .startActivity(intent)
        }

        binding.tvMyApplication.setOnClickListener {
            val intent = Intent(requireActivity(), MyApplicationActivity::class.java)
            activity?.startActivity(intent)
        }

        return binding.root
    }
}