package com.example.ljobs.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ljobs.Job.JobEntity
import com.example.ljobs.Job.JobViewModel
import com.example.ljobs.JobItemAdapter
import com.example.ljobs.R
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.User.UserViewModel
import com.example.ljobs.databinding.FragmentHomeBinding
import java.security.acl.Owner
import java.util.*


class HomeFragment() : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val jobViewModel : JobViewModel by activityViewModels()
    lateinit var session : LoginPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.rvJobList.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        session = LoginPref(activity?.applicationContext!!)
        //get current logged in user
        var user:HashMap<String,String> = session.getUserDetails()
        val email = user.get(LoginPref.KEY_EMAIL).toString()
        val userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val userName = userViewModel.fetchByEmail(email).name
        binding.Uname.text = "Welcome back $userName"

        if(user.get(LoginPref.ROLE).toString() == "3"){
            binding.Greeting.text = "Manage Ljobs to become better!"
        }

        val jobItemAdapter = JobItemAdapter(JobItemAdapter.SelectJobOnClickListener{ job -> }, this, requireActivity().applicationContext)

        if(userViewModel.fetchByEmail(email).role != "3"){
            jobViewModel.fetchByFilteringCurrentUser(email).observe(viewLifecycleOwner) {
                jobItemAdapter.setJobList(it)
                binding.rvJobList.layoutManager!!.scrollToPosition(it.size-1)
            }
        }
        else{
            jobViewModel.filterPendingJob().observe(viewLifecycleOwner) {
                jobItemAdapter.setJobList(it)
                binding.rvJobList.layoutManager!!.scrollToPosition(it.size-1)
            }
        }

        val llm = LinearLayoutManager(activity?.applicationContext)
        llm.stackFromEnd = true
        binding.rvJobList.layoutManager = llm
        binding.rvJobList.adapter = jobItemAdapter

        binding.swSearch.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(searchText: String?): Boolean {
                binding.swSearch.clearFocus()

                return false
            }

            override fun onQueryTextChange(searchText: String): Boolean {
                if(userViewModel.fetchByEmail(email).role != "3"){
                    jobViewModel.fetchByFilteringNSearchData(email,searchText).observe(viewLifecycleOwner) {
                        jobItemAdapter.setJobList(it)
                        binding.rvJobList.layoutManager!!.scrollToPosition(it.size-1)
                    }
                }
                else{
                    jobViewModel.searchData(searchText).observe(viewLifecycleOwner) {
                        jobItemAdapter.setJobList(it)
                        binding.rvJobList.layoutManager!!.scrollToPosition(it.size-1)
                    }
                }

                val llm = LinearLayoutManager(activity?.applicationContext)
                llm.stackFromEnd = true
                binding.rvJobList.layoutManager = llm
                binding.rvJobList.adapter = jobItemAdapter

                return true
            }
        })
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
            }
    }
}