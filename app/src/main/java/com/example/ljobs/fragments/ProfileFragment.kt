package com.example.ljobs.fragments

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.ljobs.R
import com.example.ljobs.UserApp
import com.example.ljobs.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentProfileBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false)

        val typeFace: Typeface = Typeface.createFromAsset(activity?.applicationContext?.assets,"goodTimesRg.otf")

        binding.tvProfilePic.setOnClickListener {
           binding.tvProfileName.text = "arecus10155@gmail.com"
        }
        binding.tvProfileName.typeface = typeFace

        // Inflate the layout for this fragment
        return binding.root
    }


}