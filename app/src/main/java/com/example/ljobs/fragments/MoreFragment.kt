package com.example.ljobs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.ljobs.R
import com.example.ljobs.databinding.FragmentMoreBinding


class MoreFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentMoreBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_more, container, false)




        return binding.root
    }
}